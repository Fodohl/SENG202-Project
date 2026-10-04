package seng202.team1.unittests;

import javafx.application.Platform;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team1.common.StringHelpers;
import seng202.team1.data.bin.*;
import seng202.team1.data.sqlite.DAO.BinDAO;
import seng202.team1.data.sqlite.DAO.StatusReportDAO;
import seng202.team1.data.sqlite.DatabaseManager;
import seng202.team1.data.table.StringTableBuilder;
import seng202.team1.data.sqlite.exceptions.InstanceAlreadyExistsException;
import seng202.team1.data.sqlite.DAO.TrashLogDAO;
import seng202.team1.gui.WindowManager;
import seng202.team1.state.GlobalState;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TrashAppTest {

    protected Random random = new Random();

    protected GlobalState state;

    protected WindowManager windowManager;

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

        // Don't draw
        GlobalState.IS_HEADLESS_MODE = true;

    }


    @BeforeEach
    void initState() throws InstanceAlreadyExistsException {
        state = new GlobalState(null, true);
        databaseManager = state.getDatabaseManager();
        databaseManager.resetDB();

        trashLogs = state.getTrashLogDAO();
        bins = state.getBinDAO();
        statusReportDAO = state.getStatusReportDAO();

        Bin testBin = new Bin(1, 0, 0, BinCategory.GENERAL_WASTE, Instant.now(), null, null, BinFullness.EMPTY);
        bins.add(testBin);

        // Extract state for easier access
        binDataset = state.getDataset();
        windowManager = state.getWindowManager();
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
