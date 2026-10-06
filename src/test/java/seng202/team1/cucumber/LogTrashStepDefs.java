package seng202.team1.cucumber;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import seng202.team1.types.bin.Bin;
import seng202.team1.types.bin.BinCategory;
import seng202.team1.types.bin.BinFullness;
import seng202.team1.types.trashlog.TrashLog;
import seng202.team1.types.trashlog.TrashType;
import seng202.team1.gui.modals.LogTrashModal;
import seng202.team1.state.GlobalState;
import seng202.team1.unittests.TrashAppTest;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seng202.team1.helpers.StringHelpers.enumNameToTitle;

public class LogTrashStepDefs {

    private LogTrashModal modal;
    private GlobalState globalState;

    @Before
    public void init() throws IOException {

        TrashAppTest.initFx();
        globalState = TrashAppTest.newTestState();
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

        globalState.getLocalTables().getBinTable().add(bin);

        modal = new LogTrashModal(globalState.getRouter());
        modal.getRendered();
        modal.recvData(bin);
    }

    @Given("the user is viewing a bin's details")
    public void theUserIsViewingABinsDetails() {
        openModal();
    }

    @When("they correctly log their usage")
    public void theyCorrectlyLogTheirUsage() {
        modal.getTypeInput().setInput(enumNameToTitle(TrashType.BANANA_PEEL.name()));
        modal.getWeightInput().setInput("500");
        modal.getFullnessInput().setInput(enumNameToTitle(BinFullness.SEMI_FULL.name()));
        modal.getSubmitButton().getOnMouseClicked().handle(null);
    }

    @Then("the logging is saved in the user's timeline")
    public void theLoggingIsSavedInTheUsersTimeline() {
        List<TrashLog> logs = getLogs();

        assertEquals(1, logs.size());
        assertEquals(TrashType.BANANA_PEEL, logs.getFirst().trashType());
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
        return globalState.getLocalTables().getTrashTable().getAll().stream()
                .filter(log -> log.binID() == 1)
                .toList();
    }

}