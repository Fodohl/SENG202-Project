package seng202.team1.cucumber;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import seng202.team1.database.DAO.BinDAO;
import seng202.team1.gui.components.UITable;
import seng202.team1.gui.pages.DataPage;
import seng202.team1.state.GlobalState;
import seng202.team1.unittests.TrashAppTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DataStepDefs {

    private UITable dataTable;
    private GlobalState globalState;

    private int BIN_SUBSET = 5;

    @Before
    public void init() throws IOException {

        TrashAppTest.initFx();
        globalState = new GlobalState(null, true);
        globalState.getDatabaseManager().resetDB();

        globalState.getDatabaseManager();

        BinDAO binDAO = globalState.getBinDAO();
        if (binDAO.getAll().isEmpty())
            binDAO.addBatch(globalState.getDataset().getBinDataset().subList(0, BIN_SUBSET));
    }


    public void openPage() {

        DataPage dataPage = new DataPage(globalState.getRouter());
        dataPage.rerender();

        dataTable = dataPage.getTable();
    }

    @Given("the user is not viewing the dataset table view")
    public void theUserIsNotViewingTheDatasetTableView() {
    }

    @When("they open the data page")
    public void theyOpenTheDataPage() {
        openPage();
    }

    @Then("all bins are shown in a table")
    public void allBinsAreShownInATable() {
        int expected = globalState.getBinDAO().getAll().size();

        assertTrue(expected > 0);
        assertEquals(expected, dataTable.getData().dataSize());
    }

}