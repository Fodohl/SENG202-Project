package seng202.team1.gui.modals;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import seng202.team1.common.StringHelpers;
import seng202.team1.data.bin.Bin;
import seng202.team1.data.bin.BinFullness;
import seng202.team1.data.bin.BinCategory;
import seng202.team1.data.status.ReportType;
import seng202.team1.data.status.StatusReport;
import seng202.team1.data.trashlog.TrashLog;
import seng202.team1.data.trashlog.TrashType;
import seng202.team1.gui.WindowManager;
import seng202.team1.gui.components.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;


/**
 * A Modal (overlay) to display trash log controls
 */
public class LogTrashModal extends Modal {

    /// The bin this issue is refering to
    private Bin bin = null;

    /// The input for what type of trash this is
    private UIDropDown typeInput;

    /// The input for how heavy this peice of trash is
    private UITextInput weightInput;

    /// The input for how full the bin is
    private UIChipsInput fullnessInput;

    /// The button to fire a submit event
    private UIButton submit;

    /**
     * Construct a new LogTrashModal
     *
     * @param windowManager The window manager responsible for this modal
     */
    public LogTrashModal(WindowManager windowManager) {
        super(windowManager, Modals.LOG_TRASH);
    }

    /**
     * Composite subcomponents into a inner UI for this modal
     *
     * @return The UI for this modal
     */
    @Override
    Parent buildBody() {

        // Prepare conents
        List<String> trashTypes = Arrays.stream(TrashType.values()).map(t -> StringHelpers.enumNameToTitle(t.name())).toList();

        // Log data
        UIIconText typeLabel = new UIIconText("bin", "Trash Type");
        typeInput = new UIDropDown("Select waste type...", trashTypes);
        UIIconText weightLabel = new UIIconText("scales", "Estimated Weight (grams)");
        weightInput = new UITextInput("Enter waste weight...");
        UISection logInputSection = new UISection(15, typeLabel, typeInput, weightLabel, weightInput);

        // Bin data
        UIIconText fullnessLabel = new UIIconText("bin-full", "Estimated Fullness");
        fullnessInput = new UIChipsInput(BinFullness.class);
        UISection binInputSection = new UISection(15, fullnessLabel, fullnessInput);

        // Actions
        submit = new UIButton("Submit Log");
        UIButton cancel = new UIButton("Cancel", true);
        UISection actionsSection = new UISection(15, submit, cancel);

        // Action handlers
        submit.setOnMouseClicked(e -> submitTrashLog());
        cancel.setOnMouseClicked(e -> closeModal());

        // Center buttons
        actionsSection.getStyleClass().add("buttons");
        actionsSection.setAlignment(Pos.CENTER);

        return new VBox(logInputSection, binInputSection, actionsSection);
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
     * Clear the form input for a subsequent report
     */
    @Override
    protected void updateContents() {

        // Cant reset yet
        if (typeInput == null || weightInput == null || fullnessInput == null)
            return;

        // Reset input
        typeInput.clear();
        weightInput.clear();
        fullnessInput.clear();
    }

     /**
     * Validate & send the collected form to the data backend
     */
    private void submitTrashLog() {

        // No bin setup
        if (bin == null)
            return;

        BinCategory category = bin.getCategory();
        Integer binID = bin.getID();

        TrashType type = null;
        Integer weight = 0;
        BinFullness fullness = null;
        Integer userID = 1;

        // Get type
        for (TrashType trashType : TrashType.values())
            if (StringHelpers.enumNameToTitle(trashType.name()).equals(typeInput.getInput())) {
                type = trashType;
            }

        // Invalid type
        if (type == null)
            typeInput.setError("Not a valid trash type");

        // No Category
        if (!typeInput.hasInput())
            typeInput.setError("Must select trash type");

        // Get weight
        try {
            weight = Integer.parseInt(weightInput.getInput());
        } catch (NumberFormatException e) {
            weightInput.setError("Weight must be a whole number");
        }

        // Cant have negative trash
        if (weight != null && weight <= 0)
            weightInput.setError("Weight must be positive");

        UIIconButton fullnessChip = (UIIconButton) fullnessInput.getInput();
        String selectedFullness = fullnessChip != null ? fullnessChip.getText() : "";

        // Get type
        for (BinFullness fullnessLevel : BinFullness.values())
            if (StringHelpers.enumNameToTitle(fullnessLevel.name()).equals(selectedFullness)) {
                fullness = fullnessLevel;
            }

        if (fullness == null)
            fullnessInput.setError("Not a valid fullness");

        // Invalid
        if (typeInput.isErroring() || weightInput.isErroring() || fullnessInput.isErroring())
            return;

        // Save
        windowManager.getGlobalState().getTrashLogDAO().add(new TrashLog(Instant.now(), Instant.now(), userID, binID, type, weight));
        windowManager.getGlobalState().getStatusReportStore().logReport(bin, fullness, ReportType._FULLNESS_UPDATE, "");

         // Refresh the bins' average status
        getWindow().getGlobalState().refreshBinFullness(bin);

        closeModal();
    }


    /**
     * Get the input of the report text
     *
     * @return The report text input component
     */
    public UITextInput getWeightInput() {
        return weightInput;
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
    public UIDropDown getTypeInput() {
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
