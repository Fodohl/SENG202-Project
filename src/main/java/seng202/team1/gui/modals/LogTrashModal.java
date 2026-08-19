package seng202.team1.gui.modals;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import seng202.team1.common.StringHelpers;
import seng202.team1.data.bin.BinFullness;
import seng202.team1.data.bin.TrashCategory;
import seng202.team1.gui.WindowManager;
import seng202.team1.gui.components.*;

import java.util.Arrays;
import java.util.List;

public class LogTrashModal extends Modal{


    private UIDropDown typeInput;
    private UITextInput weightInput;
    private UIChipsInput statusInput;

    public LogTrashModal(WindowManager windowManager){
        super(windowManager, Modals.LOG_TRASH);
    }

    @Override
    Parent buildBody() {

        // Prepare conents
        List<String> trashTypes = Arrays.stream(TrashCategory.values()).map(t -> StringHelpers.enumNameToTitle(t.name())).toList();

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


    private void submitTrashLog(){

        // Save

        // Errors
        typeInput.setError("Invalid type");
        weightInput.setError("NAN");
        statusInput.setError("To much");


//        closeTrashLog();
    }

}
