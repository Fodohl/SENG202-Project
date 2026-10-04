package seng202.team1.cucumber;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import seng202.team1.common.StringHelpers;
import seng202.team1.data.bin.Bin;
import seng202.team1.data.bin.BinCategory;
import seng202.team1.data.bin.BinFullness;
import seng202.team1.data.sqlite.DAO.BinDAO;
import seng202.team1.data.sqlite.DAO.TrashLogDAO;
import seng202.team1.data.sqlite.DatabaseManager;
import seng202.team1.data.status.ReportType;
import seng202.team1.data.status.StatusReport;
import seng202.team1.data.status.StatusReportStore;
import seng202.team1.data.trashlog.TrashLog;
import seng202.team1.data.trashlog.TrashType;
import seng202.team1.gui.modals.LogTrashModal;
import seng202.team1.state.GlobalState;
import seng202.team1.unittests.TrashAppTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LogTrashStepDefs {

    private LogTrashModal modal;
    private GlobalState globalState;

    @Before
    public void init() throws IOException {

        TrashAppTest.initFx();
        globalState = new GlobalState(null, true);
        globalState.getDatabaseManager().resetDB();
    }


    public void openModal() {

        Bin bin = new Bin(
                1,
                -43.5233,
                172.5822,
                BinCategory.GENERAL_WASTE,
                Instant.now(),
                "testParkC"
        );

        globalState.getBinDAO().add(bin);

        modal = new LogTrashModal(globalState.getWindowManager());
        modal.getRendered();
        modal.recvData(bin);
    }

    @Given("the user is viewing a bin's details")
    public void theUserIsViewingABinsDetails() {
        openModal();
    }

    @When("they correctly log their usage")
    public void theyCorrectlyLogTheirUsage() {
        modal.getTypeInput().setInput(StringHelpers.enumNameToTitle(TrashType.CARDBOARD_BOX.name()));
        modal.getWeightInput().setInput("500");
        modal.getFullnessInput().setInput(StringHelpers.enumNameToTitle(BinFullness.SEMI_FULL.name()));
        modal.getSubmitButton().getOnMouseClicked().handle(null);
    }

    @Then("the logging is saved in the user's timeline")
    public void theLoggingIsSavedInTheUsersTimeline() {
        List<TrashLog> logs = getLogs();

        assertEquals(1, logs.size());
        assertEquals(TrashType.CARDBOARD_BOX, logs.getFirst().trashType());
        assertEquals(500, logs.getFirst().trashWeight());
    }

    @Given("the user logs their usage without filling out necessary information")
    public void theUserLogsTheirUsageWithoutFillingOutNecessaryInformation() {
        openModal();
    }

    @When("they click submit")
    public void theyClickSubmit() {
        modal.getSubmitButton().getOnMouseClicked().handle(null);
    }

    @Then("the log is rejected and a message is shown to the user")
    public void theLogIsRejectedAndAMessageIsShownToTheUser() {
        assertTrue(modal.getTypeInput().isErroring());
        assertTrue(modal.getWeightInput().isErroring());
        assertTrue(modal.getFullnessInput().isErroring());
        assertTrue(getLogs().isEmpty());
    }

    private List<TrashLog> getLogs() {
        return globalState.getTrashLogDAO().getAll().stream()
                .filter(log -> log.binID() == 1)
                .toList();
    }

}