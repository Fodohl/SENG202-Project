package seng202.team1.unittests;

import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import seng202.team1.data.bin.Bin;
import seng202.team1.data.bin.BinLoader;
import seng202.team1.data.table.StringTable;
import seng202.team1.exceptions.InstanceAlreadyExistsException;
import seng202.team1.gui.WindowManager;
import seng202.team1.state.GlobalState;

import java.time.Instant;
import java.util.Map;
import java.util.Random;

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
    void initState() throws InstanceAlreadyExistsException {

        state = new GlobalState(null);

        // Extract state for easier access
        windowManager = state.getWindowManager();
        binLoader = state.getDataLoader();
    }

    protected StringTable testDataTable() {
        Map<String, String> keys = Map.of(
                                        "latitude", "lat",
                                        "longitude", "long"
                                        );
        StringTable table = new StringTable(keys);

        // Add test data
//        table.add(new Bin(1, 2, 2, null));
//        table.add(new Bin(2,2,2, null));
//        table.add(new Bin(3,3,3, null));

        return table;
    }
}
