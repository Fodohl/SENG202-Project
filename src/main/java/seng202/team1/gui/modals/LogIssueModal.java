package seng202.team1.gui.modals;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import seng202.team1.helpers.StringHelpers;
import seng202.team1.types.bin.Bin;
import seng202.team1.types.bin.BinCategory;
import seng202.team1.types.status.ReportType;
import seng202.team1.types.bin.BinFullness;
import seng202.team1.gui.Router;
import seng202.team1.gui.components.*;

import java.time.Instant;

/**
 * A Modal (overlay) to display a bin issue report
 */
public class LogIssueModal extends Modal {

    /// The bin this issue is refering to
    private Bin bin = null;

    /// The input for how full the bin is
    private UIChipsInput fullnessInput;

    /// The input for the category this issue falls under
    private UIChipsInput typeInput;

    /// The input for what the issue is with this bin
    private UIParagraphInput reportInput;

    /// The button to fire a submit event
    private UIButton submit;

    /**
     * Construct a new LogTrashModal
     *
     * @param router The router responsible for this modal
     */
    public LogIssueModal(Router router) {
        super(router, Modals.LOG_ISSUE);
    }

    /**
     * Composite subcomponents into a inner UI for this modal
     *
     * @return The UI for this modal
     */
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
        submit = new UIButton("Submit Report");
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

    /**
     * Clear the form input for a subsequent report
     */
    @Override
    protected void updateContents() {

        // Not ready
        if (typeInput == null || reportInput == null || fullnessInput == null)
            return;

        // Clear previous input
        typeInput.clear();
        reportInput.clear();
        fullnessInput.clear();

    }

    /**
     * Handle data being sent to this modal
     *
     * @param data The bin this modal is referencing
     */
    @Override
    public void recvData(Object data) {

        // Not a bin
        if (!(data instanceof Bin))
            return;

        bin = (Bin) data;
    }

    /**
     * Validate & send the collected form to the data backend
     */
    private void submitReport() {

        // No bin setup
        if (bin == null)
            return;

        BinCategory category = bin.getCategory();
        Integer binID = bin.getID();

        ReportType type = null;
        BinFullness fullness = null;
        String report = "";

        UIIconButton typeChip = (UIIconButton) typeInput.getInput();
        String selectedType = typeChip != null ? typeChip.getText() : "";

        UIIconButton fullnessChip = (UIIconButton) fullnessInput.getInput();
        String selectedFullness = fullnessChip != null ? fullnessChip.getText() : "";

        // Get type
        for (ReportType typeOption : ReportType.values())
            if (StringHelpers.enumNameToTitle(typeOption.name()).equals(selectedType)) {
                type = typeOption;
            }

        // Invalid type
        if (type == null)
            typeInput.setError("Not a valid trash type");

        // No Category
        if (!typeInput.hasInput())
            typeInput.setError("Must select trash type");

        // No Category
        if (!reportInput.hasInput())
            reportInput.setError("Must enter report");

        report = reportInput.getInput();
        if (report == null || report.isEmpty())
            reportInput.setError("Must enter report");


        // Get fullness
        for (BinFullness fullnessLevel : BinFullness.values())
            if (StringHelpers.enumNameToTitle(fullnessLevel.name()).equals(selectedFullness)) {
                fullness = fullnessLevel;
            }

        if (fullness == null)
            fullnessInput.setError("Not a valid fullness");

        // Invalid
        if (typeInput.isErroring() || reportInput.isErroring() || fullnessInput.isErroring())
            return;

        // Refresh the bins' average status
        getState().getStatusReportStore().logReport(bin, fullness, type, report);
        getState().refreshBinFullness(bin);
        getState().sync();

        closeModal();
    }

    /**
     * Get the input of the report text
     *
     * @return The report text input component
     */
    public UIParagraphInput getReportInput() {
        return reportInput;
    }

    /**
     * Get the bin linked to the modal
     *
     * @return The bin this log is referencing, null if none
     */
    public Bin getBin() {
        return bin;
    }

    /**
     * Get the button for submitting the form
     *
     * @return The submit button
     */
    public Button getSubmitButton() {
        return submit;
    }

     /**
     * Get the input of the report type
     *
     * @return The report type input component
     */
    public UIChipsInput getTypeInput() {
        return typeInput;
    }

     /**
     * Get the input of the report text
     *
     * @return The report text input component
     */
    public UIChipsInput getFullnessInput() {
        return fullnessInput;
    }
}