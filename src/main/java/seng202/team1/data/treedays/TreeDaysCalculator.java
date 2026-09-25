package seng202.team1.data.treedays;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.gson.reflect.TypeToken;

import seng202.team1.TrashApp;
import seng202.team1.data.trashlog.TrashEmission;
import seng202.team1.data.trashlog.TrashLog;
import seng202.team1.data.trashlog.TrashType;

/**
 * Calculates the total logged waste weight used by the tree-days feature.
 */
public class TreeDaysCalculator {

    private static final String EMISSIONS_PATH = "/data/trash_emissions.json";
    private static final double TREE_DAYS_PER_KG_CO2E = 60.33;
    private static final double GRAMS_PER_KG = 1000;

    private static final Map<TrashType, TrashEmission> EMISSIONS = loadEmissions();

    public static double getTreeDays(List<TrashLog> logs) {
        double runningTotal = 0;
        for (int i = 0; i < logs.size(); i++) {
            double weight = logs.get(i).trashWeight();
            TrashType type = logs.get(i).trashType();
            TrashEmission emission = EMISSIONS.get(type);

            runningTotal += (Math.max(0, (emission.litteredFactor()-emission.treatedFactor()))*(weight/GRAMS_PER_KG));
        }
        return runningTotal*TREE_DAYS_PER_KG_CO2E;
    }

    public static Optional<TrashEmission> getEmission(TrashType type) {
        return Optional.ofNullable(EMISSIONS.get(type));
    }

    private static Map<TrashType, TrashEmission> loadEmissions() {
        InputStream input = TreeDaysCalculator.class.getResourceAsStream(EMISSIONS_PATH);
        if (input == null) {
            throw new IllegalStateException("Missing resource: " + EMISSIONS_PATH);
        }

        try (Reader reader = new InputStreamReader(input, StandardCharsets.UTF_8)) {
            Type listType = new TypeToken<List<TrashEmission>>() {}.getType();
            List<TrashEmission> list = TrashApp.GSON.fromJson(reader, listType);

            Map<TrashType, TrashEmission> map = new EnumMap<>(TrashType.class);
            for (TrashEmission emission : list) {
                map.put(TrashType.valueOf(emission.id()), emission);
            }
            return Collections.unmodifiableMap(map);
        } catch (IOException e) {
            throw new IllegalStateException("Could not read " + EMISSIONS_PATH, e);
        }
    }
}
