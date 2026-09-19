package seng202.team1.unittests;

import javafx.application.Platform;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team1.helpers.StringHelpers;
import seng202.team1.data.bin.Bin;
import seng202.team1.data.bin.BinCategory;
import seng202.team1.data.bin.BinFullness;
import seng202.team1.data.bin.BinLoader;
import seng202.team1.data.repository.DatabaseManager;
import seng202.team1.data.table.StringTableBuilder;
import seng202.team1.exceptions.InstanceAlreadyExistsException;
import seng202.team1.gui.Router;
import seng202.team1.state.GlobalState;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TrashAppTest {

    protected Random random = new Random();

    protected GlobalState state;

    protected Router router;
    protected BinLoader binLoader;

    static DatabaseManager databaseManager;


    @BeforeAll
    static void initFx() {

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

    @BeforeAll
    static void initDB() throws InstanceAlreadyExistsException {
        DatabaseManager.REMOVE_INSTANCE();
        databaseManager = DatabaseManager.initialiseInstanceWithUrl("jdbc:sqlite:./src/test/resources/test_database.db");
    }


    @BeforeEach
    void initState() throws InstanceAlreadyExistsException {
        databaseManager.resetDB();
        state = GlobalState.create(null);

        // Extract state for easier access
        router = state.getRouter();
        binLoader = state.getBinLoader();

        binLoader.readGeoJSON();
    }

    @AfterEach
    void tearDown() {
        GlobalState.REMOVE_INSTANCE();
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
