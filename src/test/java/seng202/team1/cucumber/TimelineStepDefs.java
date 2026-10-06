package seng202.team1.cucumber;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import seng202.team1.types.bin.Bin;
import seng202.team1.types.bin.BinCategory;
import seng202.team1.types.bin.BinFullness;
import seng202.team1.database.DAO.BinDAO;
import seng202.team1.types.status.ReportType;
import seng202.team1.types.status.StatusReport;
import seng202.team1.types.trashlog.TrashLog;
import seng202.team1.types.trashlog.TrashType;
import seng202.team1.gui.components.UITimelineEntry;
import seng202.team1.gui.pages.TimelinePage;
import seng202.team1.state.GlobalState;
import seng202.team1.unittests.TrashAppTest;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TimelineStepDefs {

    private List<UITimelineEntry> timlineEntries;
    private GlobalState globalState;


    @Before
    public void init() throws IOException {

        TrashAppTest.initFx();
        globalState = TrashAppTest.newTestState();
    }


    public void openPage() {

        TimelinePage dataPage = new TimelinePage(globalState.getRouter());
        dataPage.rerender();

        timlineEntries = dataPage.getTimeline().getEntries();
    }

    @Given("the user has logged bin usages, and issue reports")
    public void theUserHasLoggedBinUsagesAndIssueReports() {
        Bin bin = new Bin(1, -43.5233, 172.5822, BinCategory.GENERAL_WASTE, Instant.now(), "testParkC");
        globalState.getLocalTables().getBinTable().add(bin);

        Instant now = Instant.now();
        globalState.getLocalTables().getTrashTable().add(new TrashLog(now, now, 1, bin.getID(), TrashType.BANANA_PEEL, 500));

        StatusReport reportToAdd = new StatusReport(1, 1, BinFullness.SEMI_FULL, ReportType.GRAFFITI, "Spray paint", Instant.now());
        globalState.getLocalTables().getStatusReportsTable().add(reportToAdd);

        long saved = globalState.getLocalTables().getStatusReportsTable().getAll().stream()
                .filter(r -> r.getIssue() != ReportType._FULLNESS_UPDATE)
                .count();
        assertEquals(2, saved);
    }

    @When("the user opens their timeline")
    public void theUserOpensTheirTimeline() {
       openPage();
    }

    @Then("the GUI displays a timeline of their reports and logs")
    public void theGuiDisplaysATimelineOfTheirReportsAndLogs() {
        List<String> labels = timlineEntries.stream().map(e -> e.getText()).toList();

        assertEquals(24, timlineEntries.size());
        assertTrue(labels.contains("Spray paint"));
        assertTrue(labels.contains("Plastic Bottle (500g) logged at testParkC"));

    }
}