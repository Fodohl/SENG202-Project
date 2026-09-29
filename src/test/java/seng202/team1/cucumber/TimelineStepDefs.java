package seng202.team1.cucumber;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import seng202.team1.data.bin.Bin;
import seng202.team1.data.bin.BinCategory;
import seng202.team1.data.bin.BinFullness;
import seng202.team1.data.sqlite.DAO.BinDAO;
import seng202.team1.data.status.ReportType;
import seng202.team1.data.trashlog.TrashLog;
import seng202.team1.data.trashlog.TrashType;
import seng202.team1.gui.components.UITable;
import seng202.team1.gui.components.UITimeline;
import seng202.team1.gui.components.UITimelineEntry;
import seng202.team1.gui.pages.DataPage;
import seng202.team1.gui.pages.TimelinePage;
import seng202.team1.state.GlobalState;
import seng202.team1.unittests.TrashAppTest;

import java.io.IOException;
import java.time.Duration;
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
        globalState = new GlobalState(null, true);
        globalState.getDatabaseManager().resetDB();

        globalState.getDatabaseManager();

        BinDAO binDAO = globalState.getBinDAO();
    }


    public void openPage() {

        TimelinePage dataPage = new TimelinePage(globalState.getWindowManager());
        dataPage.rerender();

        timlineEntries = dataPage.getTimeline().getEntries();
    }

    @Given("the user has logged bin usages, and issue reports")
    public void theUserHasLoggedBinUsagesAndIssueReports() {
        Bin bin = new Bin(1, -43.5233, 172.5822, BinCategory.GENERAL_WASTE, Instant.now(), "testParkC");
        globalState.getBinDAO().add(bin);

        Instant now = Instant.now();
        globalState.getTrashLogDAO().add(new TrashLog(now, now, 1, bin.getID(), TrashType.BANANA_PEEL, 500));

        globalState.getStatusReportStore().logReport(bin, BinFullness.SEMI_FULL, ReportType.GRAFFITI, "Spray paint");

        long saved = globalState.getStatusReportStore().getReports().stream()
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