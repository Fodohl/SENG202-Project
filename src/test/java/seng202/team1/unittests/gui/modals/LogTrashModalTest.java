package seng202.team1.unittests.gui.modals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team1.types.bin.Bin;
import seng202.team1.gui.components.UIChipsInput;
import seng202.team1.gui.components.UIDropDown;
import seng202.team1.gui.components.UITextInput;
import seng202.team1.gui.modals.LogTrashModal;
import seng202.team1.gui.modals.Modals;
import seng202.team1.unittests.TrashAppTest;

import static org.junit.jupiter.api.Assertions.*;

class LogTrashModalTest extends TrashAppTest {

   private LogTrashModal modal;
    private Bin testBin;

    @BeforeEach
    void init() {
        testBin = testData().getFirst();

        modal = new LogTrashModal(router);
        modal.getRendered();
    }

    @Test
    void modalShouldClearFormOnceClosed() {
        UITextInput weightInput = modal.getWeightInput();
        weightInput.setInput("25");

        router.openModal(Modals.LOG_TRASH, testBin);
        router.closeModal();

        assertFalse(weightInput.hasInput());
    }

    @Test
    void modalShouldOnlyTakeBinAsData() {
        modal.recvData("Not A Bin Object");
        assertNull(modal.getBin());

        modal.recvData(testBin);
        assertEquals(testBin, modal.getBin());
    }

    @Test
    void shouldErrorIfTrashWeightIsntInt() {
        modal.recvData(testBin);

        UITextInput weightInput = modal.getWeightInput();
        weightInput.setInput("nan");

        modal.getSubmitButton().getOnMouseClicked().handle(null);

        assertTrue(weightInput.isErroring());
    }

    @Test
    void mustSelectATrashCategory() {
        modal.recvData(testBin);
        modal.getSubmitButton().getOnMouseClicked().handle(null);

        UIDropDown typeInput = modal.getTypeInput();
        assertTrue(typeInput.isErroring());
    }

    @Test
    void mustEnterTrashWeight() {
        modal.recvData(testBin);
        modal.getSubmitButton().getOnMouseClicked().handle(null);

        UITextInput weightInput = modal.getWeightInput();
        assertTrue(weightInput.isErroring());
    }

    @Test
    void mustSelectABinFullness() {
        modal.recvData(testBin);
        modal.getSubmitButton().getOnMouseClicked().handle(null);

        UIChipsInput fullnessInput = modal.getFullnessInput();
        assertTrue(fullnessInput.isErroring());
    }

}