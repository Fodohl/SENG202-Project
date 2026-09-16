package seng202.team1.gui.modals;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import seng202.team1.common.StringHelpers;
import seng202.team1.data.bin.Bin;
import seng202.team1.data.bin.BinCategory;
import seng202.team1.data.status.ReportType;
import seng202.team1.data.bin.BinFullness;
import seng202.team1.data.trashlog.TrashType;
import seng202.team1.gui.WindowManager;
import seng202.team1.gui.components.*;

public class LogIssueModal extends Modal{


    private Bin bin = null;

    private UIChipsInput fullnessInput;
    private UIChipsInput typeInput;
    private UIParagraphInput reportInput;

    public LogIssueModal(WindowManager windowManager){
        super(windowManager, Modals.LOG_ISSUE);
    }

    @Override
    Parent buildBody() {

        // Bin data
        UIIconText reportLabel = new UIIconText("log", "Report Info");
        reportInput = new UIParagraphInput("Enter the issue with this bin...");
        UIIconText typeLabel = new UIIconText("tag", "Report Type");
        typeInput = new UIChipsInput(ReportType.class);
        UIIconText statusLabel = new UIIconText("bin-full", "Estimated Fullness");
        fullnessInput = new UIChipsInput(BinFullness.class);
        UISection binInputSection = new UISection(15, reportLabel, reportInput, typeLabel, typeInput, statusLabel, fullnessInput);

        // Actions
        UIButton submit = new UIButton("Submit Report");
        UIButton cancel = new UIButton("Cancel", true);
        UISection actionsSection = new UISection(15, submit, cancel);

        // Action handlers
        submit.setOnMouseClicked(e -> submitReport());
        cancel.setOnMouseClicked(e -> closeModal());

        // Center buttons
        actionsSection.getStyleClass().add("buttons");
        actionsSection.setAlignment(Pos.CENTER);

        return new VBox(binInputSection, actionsSection);
    }

    @Override
    protected void updateContents() {

        // Not ready
        if(reportInput == null || fullnessInput == null)
            return;

        // Clear previous input
        reportInput.clear();
        fullnessInput.clear();

    }

    @Override
    public void recvData(Object data) {

        // Not a bin
        if(!(data instanceof Bin))
            return;

        bin = (Bin)data;
    }

    private void submitReport(){

        // No bin setup (@todo log error)
        if(bin == null)
            return;

        BinCategory category = bin.getCategory();
        Integer binID = bin.getID();

        ReportType type = null;
        BinFullness fullness = null;
        String report = "";

        UIIconButton typeChip = (UIIconButton)typeInput.getInput();
        String selectedType = typeChip.getText();

        UIIconButton fullnessChip = (UIIconButton)fullnessInput.getInput();
        String selectedFullness = fullnessChip.getText();

        // Get type
        for(ReportType typeOption : ReportType.values())
            if(StringHelpers.enumNameToTitle(typeOption.name()).equals(selectedType)){
                type = typeOption;
            }

        // Invalid type
        if(type == null)
            typeInput.setError("Not a valid trash type");

        // No Category
        if(!typeInput.hasInput())
            typeInput.setError("Must select trash type");

        // No Category
        if(!reportInput.hasInput())
            reportInput.setError("Must enter report");

        report = reportInput.getInput();
        if(report.isEmpty())
            reportInput.setError("Must enter report");


        // Get fullness
        for(BinFullness fullnessLevel : BinFullness.values())
            if(StringHelpers.enumNameToTitle(fullnessLevel.name()).equals(selectedFullness)){
                fullness = fullnessLevel;
            }

        if(fullness == null)
            fullnessInput.setError("Not a valid fullness");

        // Invalid
        if(typeInput.isErroring() ||  reportInput.isErroring() || fullnessInput.isErroring())
            return;


        windowManager.getGlobalState().getStatusReportStore().logReport(bin, fullness, type, report);
        closeModal();
    }
}