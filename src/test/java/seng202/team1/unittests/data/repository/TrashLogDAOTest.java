package seng202.team1.unittests.data.repository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seng202.team1.data.bin.Bin;
import seng202.team1.data.bin.BinCategory;
import seng202.team1.data.bin.BinFullness;
import seng202.team1.data.trashlog.TrashLog;
import seng202.team1.data.trashlog.TrashType;
import seng202.team1.exceptions.InstanceAlreadyExistsException;
import seng202.team1.data.repository.BinDAO;
import seng202.team1.data.repository.DatabaseManager;
import seng202.team1.data.repository.TrashLogDAO;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class TrashLogDAOTest {

    static TrashLogDAO trashLogDAO;
    static DatabaseManager databaseManager;
    static BinDAO binDAO;

    @BeforeAll
    static void setup() throws InstanceAlreadyExistsException {
        DatabaseManager.REMOVE_INSTANCE();
        databaseManager = DatabaseManager.initialiseInstanceWithUrl("jdbc:sqlite:./src/test/resources/test_database.db");
        trashLogDAO = new TrashLogDAO();
        binDAO = new BinDAO();
        Bin testBin = new Bin(1, 0, 0, BinCategory.GENERAL_WASTE, Instant.now(), null, null);
        binDAO.add(testBin);
    }

    @BeforeEach
    void resetDB() {
        databaseManager.resetDB();
    }

    @Test
    void testAddTrashLog() {
        TrashLog trashLogToAdd = new TrashLog(Instant.now(), Instant.now(), 1, 1, TrashType.CARDBOARD_BOX_SMALL, 0);
        trashLogs.add(trashLogToAdd);
        Assertions.assertEquals(1, bins.getAll().size());
        TrashLog trashLog = trashLogs.getAll().getFirst();
        Instant expectedCreatedAt = trashLogToAdd.createdAt().truncatedTo(ChronoUnit.MILLIS);
        Assertions.assertEquals(expectedCreatedAt, trashLog.createdAt());
        Instant expectedDisposedAt = trashLogToAdd.createdAt().truncatedTo(ChronoUnit.MILLIS);
        Assertions.assertEquals(expectedDisposedAt, trashLog.disposedAt());
        Assertions.assertEquals(trashLogToAdd.userID(), trashLog.userID());
        Assertions.assertEquals(trashLogToAdd.binID(), trashLog.binID());
        Assertions.assertEquals(trashLogToAdd.trashType(), trashLog.trashType());
        Assertions.assertEquals(trashLogToAdd.trashWeight(), trashLog.trashWeight());
    }

    @Test
    void testGetTrashByID() {
        TrashLog trashLogToAdd = new TrashLog(Instant.now(), Instant.now(), 1, 1, TrashType.OFFICE_PAPER_SHEET, 0);
        int id = trashLogs.add(trashLogToAdd);
        TrashLog returnedTrashLog = trashLogs.getOne(id);
        Instant expectedCreatedAt = trashLogToAdd.createdAt().truncatedTo(ChronoUnit.MILLIS);
        Assertions.assertEquals(expectedCreatedAt, returnedTrashLog.createdAt());
        Instant expectedDisposedAt = trashLogToAdd.createdAt().truncatedTo(ChronoUnit.MILLIS);
        Assertions.assertEquals(expectedDisposedAt, returnedTrashLog.disposedAt());
        Assertions.assertEquals(trashLogToAdd.userID(), returnedTrashLog.userID());
        Assertions.assertEquals(trashLogToAdd.binID(), returnedTrashLog.binID());
        Assertions.assertEquals(trashLogToAdd.trashType(), returnedTrashLog.trashType());
        Assertions.assertEquals(trashLogToAdd.trashWeight(), returnedTrashLog.trashWeight());
    }

    @Test
    void testDeleteTrash() {
        int insertId  = trashLogs.add(new TrashLog(Instant.now(), Instant.now(), 1, 1, TrashType.CARDBOARD_BOX_SMALL, 0));
        int totalTrashBefore = trashLogs.getAll().size();
        trashLogs.delete(insertId);
        Assertions.assertEquals(totalTrashBefore - 1, trashLogs.getAll().size());
    }

    @Test
    void testUpdateTrash() {
        TrashLog trashLog = new TrashLog(Instant.now(), Instant.now(), 1, 1, TrashType.CARDBOARD_BOX_SMALL, 0);
        int id = trashLogs.add(trashLog);
        TrashLog updatedTrashLog = new TrashLog(id, Instant.now(), Instant.now(), 1, 1, TrashType.BANANA_PEEL, 50);
        trashLogs.update(updatedTrashLog);
        TrashLog result = trashLogs.getOne(id);

        Instant expectedCreatedAt = trashLog.createdAt().truncatedTo(ChronoUnit.MILLIS);
        Assertions.assertEquals(expectedCreatedAt, result.createdAt());

        Instant expectedDisposedAt = updatedTrashLog.disposedAt().truncatedTo(ChronoUnit.MILLIS);
        Assertions.assertEquals(expectedDisposedAt, result.disposedAt());

        Assertions.assertEquals(updatedTrashLog.userID(), result.userID());
        Assertions.assertEquals(updatedTrashLog.binID(), result.binID());
        Assertions.assertEquals(updatedTrashLog.trashType(), result.trashType());
        Assertions.assertEquals(updatedTrashLog.trashWeight(), result.trashWeight());
    }

}

