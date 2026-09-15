package seng202.team1.gui.modals;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import seng202.team1.data.status.ReportType;
import seng202.team1.data.bin.BinFullness;
import seng202.team1.gui.WindowManager;
import seng202.team1.gui.components.*;

public class LogIssueModal extends Modal{


    private UIChipsInput statusInput;
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
        statusInput = new UIChipsInput(BinFullness.class);
        UISection binInputSection = new UISection(15, reportLabel, reportInput, typeLabel, typeInput, statusLabel, statusInput);

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
        if(reportInput == null || statusInput == null)
            return;

        // Clear previous input
        reportInput.clear();
        statusInput.clear();

    }

    private void submitReport(){

        // Save

        // Errors
        reportInput.setError("Not enough chars");
        typeInput.setError("tag err");
        statusInput.setError("To much");

//        closeTrashLog();
    }
}
