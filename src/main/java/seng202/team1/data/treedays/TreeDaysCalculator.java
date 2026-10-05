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
import seng202.team1.types.trashlog.TrashEmission;
import seng202.team1.types.trashlog.TrashLog;
import seng202.team1.types.trashlog.TrashType;

/**
 * Calculates the tree-days of all of the logged trash.
 */
public class TreeDaysCalculator {

    private static final String EMISSIONS_PATH = "/data/trash_emissions.json";
    private static final double TREE_DAYS_PER_KG_CO2E = 60.33;
    private static final double GRAMS_PER_KG = 1000;

    /// Emission info for each trash type
    private static final Map<TrashType, TrashEmission> EMISSIONS = loadEmissions();

    /**
     * Calculates the total tree-days saved by the given logs.
     *
     * @param logs The trash logs to add
     * @return The tree-days saved
     */
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

    /**
     * Gets the emission info for a trash type
     *
     * @param type The trash type
     * @return Its emission info, or empty if it has none
     */
    public static Optional<TrashEmission> getEmission(TrashType type) {
        return Optional.ofNullable(EMISSIONS.get(type));
    }

    /**
     * Reads trash_emissions.json into a map by trash type
     *
     * @return The emission info for each trash type
     * @throws IllegalStateException if the file is missing
     * @throws IllegalArgumentException if an id doesn't match a {@link TrashType}
     */
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
