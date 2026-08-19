package seng202.team1.gui.modals;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import seng202.team1.common.StringHelpers;
import seng202.team1.data.bin.Bin;
import seng202.team1.data.bin.BinFullness;
import seng202.team1.data.bin.BinCategory;
import seng202.team1.data.trashlog.TrashType;
import seng202.team1.gui.WindowManager;
import seng202.team1.gui.components.*;

import java.util.Arrays;
import java.util.List;

//@todo confimation icon
//@todo animate components

public class LogTrashModal extends Modal{

    private Bin bin = null;

    private UIDropDown typeInput;
    private UITextInput weightInput;
    private UIChipsInput statusInput;

    public LogTrashModal(WindowManager windowManager){
        super(windowManager, Modals.LOG_TRASH);
    }

    @Override
    Parent buildBody() {

        // Prepare conents
        List<String> trashTypes = Arrays.stream(TrashType.values()).map(t -> StringHelpers.enumNameToTitle(t.name())).toList();

        // Log data
        UIIconText typeLabel = new UIIconText("bin", "Trash Type");
        typeInput = new UIDropDown("Select waste type...", trashTypes);
        UIIconText weightLabel = new UIIconText("scales", "Estimated Weight");
        weightInput = new UITextInput("Enter waste weight...");
        UISection logInputSection = new UISection(15, typeLabel, typeInput, weightLabel, weightInput);

        // Bin data
        UIIconText statusLabel = new UIIconText("bin-full", "Estimated Fullness");
        statusInput = new UIChipsInput(BinFullness.class);
        UISection binInputSection = new UISection(15, statusLabel, statusInput);

        // Actions
        UIButton submit = new UIButton("Submit Log");
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

    @Override
    public void recvData(Object data) {

        // Not a bin
        if(!(data instanceof Bin))
            return;

        bin = (Bin)data;
    }

    @Override
    protected void updateContents() {

        // Cant reset yet
        if(typeInput == null || weightInput == null || statusInput == null)
            return;

        // Reset input
        typeInput.clear();
        weightInput.clear();
        statusInput.clear();
    }

    private void submitTrashLog(){

        // No bin setup (@todo log error)
        if(bin == null)
            return;

        BinCategory category = bin.getCategory();
        Integer binID = bin.getID();

        TrashType type = null;
        Integer weight = null;

        // Get type
        for(TrashType trashType : TrashType.values())
            if(StringHelpers.enumNameToTitle(trashType.name()).equals(typeInput.getInput())){
                type = trashType;
            }

        // Invalid type
        if(type == null)
            typeInput.setError("Not a valid trash type");

        // No Category
        if(!typeInput.hasInput())
            typeInput.setError("Must select trash type");

        // Get weight
        try{
            weight = Integer.parseInt(weightInput.getInput());
        } catch (NumberFormatException e) {
            weightInput.setError("Weight must be a whole number");
        }

        // Cant have negative trash
        if(weight != null && weight <= 0)
            weightInput.setError("Weight must be positive");

        //@todo merge in
        statusInput.setError("not implemented");

        // Invalid
        if(typeInput.isErroring() ||  weightInput.isErroring())// || statusInput.isErroring())
            return;

        // Save
        windowManager.getGlobalState().getTrashLogStore().logTrash(category, type, weight, binID);

        closeModal();
    }

}
