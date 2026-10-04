package seng202.team1.unittests.data.table;

import org.junit.jupiter.api.Test;
import seng202.team1.types.bin.Bin;
import seng202.team1.types.bin.BinCategory;
import seng202.team1.data.table.StringTable;
import seng202.team1.data.table.StringTableBuilder;
import seng202.team1.unittests.TrashAppTest;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StringTableBuilderTest extends TrashAppTest {


    @Test
    void stringTableShouldUseMostRecentSort() {

        StringTableBuilder<Bin> builder = new StringTableBuilder<>(testData())
                .addColumn("binID", "ID")
                .addColumn("areaName", "Location")
                .sortBy("binID", true)
                .sortBy("areaName", false);

        StringTable table = builder.build();

        assertEquals("testParkC", table.getDataRow(0).get("Location"));
        assertEquals("testParkB", table.getDataRow(1).get("Location"));
        assertEquals("testParkA", table.getDataRow(2).get("Location"));
    }

    @Test
    void stringTableDataShouldReflectivlyUpdate() {
         StringTableBuilder<Bin> builder = new StringTableBuilder<>(testData())
                .addColumn("areaName", "Location");

        List<Bin> newData = List.of(new Bin(1,1,1, BinCategory.GENERAL_WASTE, Instant.now(), "testParkUpdated"));
        builder.updateData(newData);

        StringTable table = builder.build();

        assertEquals(1, table.dataSize());
        assertEquals("testParkUpdated", table.getDataRow(0).get("Location"));
    }

    @Test
    void addingColumShouldHozGrowTable() {
          StringTableBuilder<Bin> builder = new StringTableBuilder<>(testData())
                .addColumn("binID", "ID")
                .addColumn("areaName", "Location");

        StringTable table = builder.build();
        assertEquals(2, table.getHeaderRow().size());

        builder.addColumn("type", "Type");
        table = builder.build();
        assertEquals(3, table.getHeaderRow().size());

    }

    @Test
    void formattersShouldApply() {
         StringTableBuilder<Bin> builder = new StringTableBuilder<>(testData())
                .addColumn("binID", "ID", id -> "#" + id);

        StringTable table = builder.build();

        assertEquals("#1", table.getDataRow(0).get("ID"));
        assertEquals("#2", table.getDataRow(1).get("ID"));
        assertEquals("#3", table.getDataRow(2).get("ID"));
    }
}