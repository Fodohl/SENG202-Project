package seng202.team1.cucumber;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import seng202.team1.data.bin.Bin;
import seng202.team1.data.bin.BinCategory;
import seng202.team1.data.bin.BinFullness;
import seng202.team1.data.sqlite.DAO.BinDAO;
import seng202.team1.data.sqlite.DAO.TrashLogDAO;
import seng202.team1.data.sqlite.DatabaseManager;
import seng202.team1.data.status.ReportType;
import seng202.team1.data.status.StatusReport;
import seng202.team1.data.status.StatusReportStore;
import seng202.team1.gui.modals.LogIssueModal;
import seng202.team1.gui.modals.LogTrashModal;
import seng202.team1.helpers.StringHelpers;
import seng202.team1.state.GlobalState;
import seng202.team1.unittests.TrashAppTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LogIssueStepDefs {

     private LogIssueModal modal;
    private GlobalState globalState;

    @Before
    public void init() throws IOException {

        TrashAppTest.initFx();
        globalState = new GlobalState(null, true);

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

        modal = new LogIssueModal(globalState.getRouter());
        modal.getRendered();
        modal.recvData(bin);
    }

    @Given("the user enters a valid issue report")
    public void theUserEntersAValidIssueReport() {
        openModal();

        modal.getTypeInput().setInput(StringHelpers.enumNameToTitle(ReportType.FIRE_HAZARD.name()));
        modal.getReportInput().setInput("bin overcooked");
        modal.getFullnessInput().setInput(StringHelpers.enumNameToTitle(BinFullness.FULL.name()));
    }

    @When("they select submit report")
    public void theySelectSubmitReport() {
        modal.getSubmitButton().getOnMouseClicked().handle(null);
    }

    @Then("the report is added to the user's timeline and appears on the bin's details")
    public void theReportIsAddedToTheUsersTimelineAndAppearsOnTheBinsDetails() {

        List<StatusReport> reports = getReports();

        Assertions.assertEquals(1, reports.size());
        Assertions.assertEquals(ReportType.FIRE_HAZARD, reports.getFirst().getIssue());
        Assertions.assertEquals("bin overcooked", reports.getFirst().getDescription());
    }

    @Given("the user enters an invalid issue report")
    public void theUserEntersAnInvalidIssueReport() {
        openModal();
    }

    @Then("the report is rejected and an error message is shown to the user")
    public void theReportIsRejectedAndAnErrorMessageIsShownToTheUser() {
        Assertions.assertTrue(modal.getReportInput().isErroring());
        Assertions.assertTrue(modal.getTypeInput().isErroring());
        Assertions.assertTrue(modal.getFullnessInput().isErroring());
        Assertions.assertTrue(getReports().isEmpty());
    }

     private List<StatusReport> getReports() {
        return globalState.getStatusReportStore().getReports().stream()
                .filter(r -> r.getBinID() == 1)
                .filter(r -> r.getIssue() != ReportType._FULLNESS_UPDATE)
                .toList();
    }
}