package seng202.team1.unittests;

import javafx.application.Platform;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team1.database.DAO.StatusReportDAO;
import seng202.team1.helpers.StringHelpers;
import seng202.team1.types.bin.Bin;
import seng202.team1.types.bin.BinCategory;
import seng202.team1.types.bin.BinFullness;
import seng202.team1.database.DAO.BinDAO;
import seng202.team1.database.DatabaseManager;
import seng202.team1.data.table.StringTableBuilder;
import seng202.team1.database.exceptions.InstanceAlreadyExistsException;
import seng202.team1.database.DAO.TrashLogDAO;
import seng202.team1.gui.Router;
import seng202.team1.state.GlobalState;
import seng202.team1.data.bin.BinDataset;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TrashAppTest {

    protected Random random = new Random();

    protected GlobalState state;

    protected Router router;

    protected DatabaseManager databaseManager;

    protected TrashLogDAO trashLogs;
    protected BinDAO bins;
    protected StatusReportDAO statusReportDAO;
    protected BinDataset binDataset;


    @BeforeAll
    public static void initFx() {

        // Make sure javafx has started
        try {
            Platform.startup(() -> {
            });

        } catch (IllegalStateException e) {
            // already running
        }

    }

    public static GlobalState newTestState() {

        GlobalState.IS_TEST_MODE = true;
        GlobalState.IS_HEADLESS_MODE = true;

        GlobalState globalState = new GlobalState(null);
        globalState.getDatabaseManager().resetDB();

        return globalState;
    }


    @BeforeEach
    void initState() throws InstanceAlreadyExistsException {
        state = newTestState();

        databaseManager = state.getDatabaseManager();
        trashLogs = state.getTrashLogDAO();
        bins = state.getBinDAO();
        statusReportDAO = state.getStatusReportDAO();

        Bin testBin = new Bin(1, 0, 0, BinCategory.GENERAL_WASTE, Instant.now(), null, null, BinFullness.EMPTY);
        bins.add(testBin);

        // Extract state for easier access
        router = state.getRouter();
        binDataset = state.getDataset();
    }

    @AfterEach
    void tearDown() {
        binDataset = state.getDataset();
        router = state.getRouter();
    }

    protected ArrayList<Bin> testData(){

         return new ArrayList<>(List.of(new Bin(1,1,1, BinCategory.GENERAL_WASTE, Instant.now(), "testParkA"),
                                 new Bin(2,2,2, BinCategory.GENERAL_WASTE, Instant.now(), "testParkB"),
                                 new Bin(3,3,3, BinCategory.GENERAL_WASTE, Instant.now(), "testParkC")
         ));
    }

    @Test
    void usesTestDatabase() {
        assertEquals("jdbc:sqlite:./src/test/resources/test_database.db", databaseManager.getUrl());
    }

    protected StringTableBuilder<Bin> testDataTable() {

        // Add test data
        ArrayList<Bin> data = testData();

        StringTableBuilder<Bin> table = new StringTableBuilder<>(data)
                                        .addColumn("binID", "ID")
                                        .addColumn("fullness", "Fullness", val -> StringHelpers.enumNameToTitle(((BinFullness)val).name()))
                                        .addColumn("areaName", "Location")
                                        .addColumn("type", "Type", val -> StringHelpers.enumNameToTitle(((BinCategory)val).name()))
                                        .sortBy(Comparator.comparing(item -> item.getID()));
        return table;
    }
}
