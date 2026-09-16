package seng202.team1.unittests;

import io.cucumber.java.bs.A;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import seng202.team1.common.StringHelpers;
import seng202.team1.data.bin.Bin;
import seng202.team1.data.bin.BinCategory;
import seng202.team1.data.bin.BinFullness;
import seng202.team1.data.bin.BinLoader;
import seng202.team1.data.table.StringTable;
import seng202.team1.data.table.StringTableBuilder;
import seng202.team1.gui.WindowManager;
import seng202.team1.state.GlobalState;

import java.time.Instant;
import java.util.*;

public class TrashAppTest {

    protected Random random = new Random();

    protected GlobalState state;

    protected WindowManager windowManager;
    protected BinLoader binLoader;

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

    @BeforeEach
    void initState(){

        state = new GlobalState(null);

        // Extract state for easier access
        windowManager = state.getWindowManager();
        binLoader = state.getDataLoader();

        binLoader.readGeoJSON();
    }

    protected ArrayList<Bin> testData(){

         return new ArrayList<>(List.of(new Bin(1,1,1, BinCategory.GENERAL_WASTE, Instant.now(), "testPark"),
                                 new Bin(2,2,2, BinCategory.GENERAL_WASTE, Instant.now(), "testPark"),
                                 new Bin(3,3,3, BinCategory.GENERAL_WASTE, Instant.now(), "testPark")
         ));
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
