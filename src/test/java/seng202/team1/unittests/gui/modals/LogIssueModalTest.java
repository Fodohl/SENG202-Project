package seng202.team1.unittests.gui.modals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team1.data.bin.Bin;
import seng202.team1.gui.components.UIChipsInput;
import seng202.team1.gui.components.UIParagraphInput;
import seng202.team1.gui.modals.LogIssueModal;
import seng202.team1.gui.modals.Modals;
import seng202.team1.unittests.TrashAppTest;

import static org.junit.jupiter.api.Assertions.*;

class LogIssueModalTest extends TrashAppTest {

    private LogIssueModal modal;
    private Bin testBin;

    @BeforeEach
    void init() {
        modal = new LogIssueModal(router);
        testBin = testData().getFirst();

        // Setup fields
        modal.getRendered();
        router.openModal(Modals.LOG_ISSUE, testBin);
    }

    @Test
    void modalShouldClearFormOnceClosed() {
        UIParagraphInput reportInput = modal.getReportInput();
        reportInput.setInput("Test report description");

        router.openModal(Modals.LOG_ISSUE, testBin);
        router.closeModal();

        assertFalse(reportInput.hasInput());
    }

    @Test
    void modalShouldOnlyTakeBinAsData() {
        modal.recvData("str");
        assertNull(modal.getBin());

        modal.recvData(testBin);
        assertEquals(testBin, modal.getBin());
    }

    @Test
    void modalShouldErrorIfReportIsEmpty() {
        modal.recvData(testBin);
        modal.getSubmitButton().getOnMouseClicked().handle(null);

        UIParagraphInput reportInput = modal.getReportInput();
        assertTrue(reportInput.isErroring());
    }

    @Test
    void mustSelectAReportType() {
       modal.recvData(testBin);
        modal.getSubmitButton().getOnMouseClicked().handle(null);

        UIChipsInput typeInput = modal.getTypeInput();
        assertTrue(typeInput.isErroring());
    }

    @Test
    void mustSelectABinFullness() {
        modal.recvData(testBin);
        modal.getSubmitButton().getOnMouseClicked().handle(null);

        UIChipsInput fullnessInput = modal.getFullnessInput();
        assertTrue(fullnessInput.isErroring());
    }

}