package seng202.team1.unittests.data.treedays;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import seng202.team1.types.trashlog.TrashLog;
import seng202.team1.types.trashlog.TrashType;
import seng202.team1.data.treedays.TreeDaysCalculator;
/**
 * Test TreeDaysCalculator
 * @author Stanley Easthope
 */
public class TreeDaysCalculatorTest {
    private static final double DELTA = 0.0001;
    private static final double TREE_DAYS_PER_KG_CO2E = 60.33;

    private TrashLog log(TrashType type, double grams) {
        return new TrashLog(Instant.now(), Instant.now(), 1, 1, type, grams);
    }

    /**
     * No logs means no tree-days
     */
    @Test
    public void testEmptyLogsGiveZero() {
        assertEquals(0, TreeDaysCalculator.getTreeDays(List.of()), DELTA);
    }

    /**
     * 1 kg of aluminium cans matches calculated value
     */
    @Test
    public void testKnownItem() {
        // litteredFactor 0.022, treatedFactor -10.0641
        double expected = (0.022 - -10.0641) * 1 * TREE_DAYS_PER_KG_CO2E;

        assertEquals(expected, TreeDaysCalculator.getTreeDays(List.of(log(TrashType.ALUMINIUM_CAN_330ML, 1000))), DELTA);
    }

    /**
     * Double weight
     */
    @Test
    public void testScalesWithWeight() {
        double half = TreeDaysCalculator.getTreeDays(List.of(log(TrashType.STEEL_FOOD_CAN, 500)));
        double full = TreeDaysCalculator.getTreeDays(List.of(log(TrashType.STEEL_FOOD_CAN, 1000)));

        assertEquals(full, half * 2, DELTA);
    }

    /**
     * Several in one list vs one each
     */
    @Test
    public void testSumsMultipleLogs() {
        TrashLog can = log(TrashType.ALUMINIUM_CAN_330ML, 13);
        TrashLog paper = log(TrashType.NEWSPAPER, 200);

        double separate = TreeDaysCalculator.getTreeDays(List.of(can)) + TreeDaysCalculator.getTreeDays(List.of(paper));

        assertEquals(separate, TreeDaysCalculator.getTreeDays(List.of(can, paper)), DELTA);
    }

    /**
     * OTHER is logged and saves 0
     */
    @Test
    public void testOtherGivesZero() {
        assertEquals(0, TreeDaysCalculator.getTreeDays(List.of(log(TrashType.OTHER, 500))), DELTA);
    }

    /**
     * non-recyclable plastics is 0
     */
    @Test
    public void testNoSavingItemGivesZero() {
        assertEquals(0, TreeDaysCalculator.getTreeDays(List.of(log(TrashType.PLASTIC_SHOPPING_BAG, 500))), DELTA);
    }

    /**
     * No item gives negative
     */
    @Test
    public void testNeverNegative() {
        for (TrashType type : TrashType.values()) {
            double treeDays = TreeDaysCalculator.getTreeDays(List.of(log(type, 1000)));
            assertTrue(treeDays >= 0, type + " gave negative tree-days: " + treeDays);
        }
    }

    /**
     * Every TrashType has an entry in the JSON
     */
    @Test
    public void testEveryTypeHasEmissionData() {
        for (TrashType type : TrashType.values()) {
            assertTrue(TreeDaysCalculator.getEmission(type).isPresent(), type + " is missing from trash_emissions.json");
        }
    }

    /**
     * Each type stored into rigbt id
     */
    @Test
    public void testEmissionMatchesType() {
        for (TrashType type : TrashType.values()) {
            TreeDaysCalculator.getEmission(type)
                    .ifPresent(emission -> assertEquals(type.name(), emission.id()));
        }
    }

}
