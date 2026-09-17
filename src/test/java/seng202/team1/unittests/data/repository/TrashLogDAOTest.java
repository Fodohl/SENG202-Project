package seng202.team1.unittests.data.repository;

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
        TrashLog trashLogToAdd = new TrashLog(Instant.now(), Instant.now(), 1, 1, TrashType.COFFEE_CUP, 0);
        trashLogDAO.add(trashLogToAdd);
        Assertions.assertEquals(1, trashLogDAO.getAll().size());
        TrashLog trashLog = trashLogDAO.getAll().getFirst();
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
        TrashLog trashLogToAdd = new TrashLog(Instant.now(), Instant.now(), 1, 1, TrashType.PAPER, 0);
        int id = trashLogDAO.add(trashLogToAdd);
        TrashLog returnedTrashLog = trashLogDAO.getOne(id);
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
        int insertId  = trashLogDAO.add(new TrashLog(Instant.now(), Instant.now(), 1, 1, TrashType.COFFEE_CUP, 0));
        int totalTrashBefore = trashLogDAO.getAll().size();
        trashLogDAO.delete(insertId);
        Assertions.assertEquals(totalTrashBefore - 1, trashLogDAO.getAll().size());
    }

    @Test
    void testUpdateTrash() {
        TrashLog trashLog = new TrashLog(Instant.now(), Instant.now(), 1, 1, TrashType.COFFEE_CUP, 0);
        int id = trashLogDAO.add(trashLog);
        TrashLog updatedTrashLog = new TrashLog(id, Instant.now(), Instant.now(), 1, 1, TrashType.PLASTIC_BOTTLE, 50);
        trashLogDAO.update(updatedTrashLog);
        TrashLog result = trashLogDAO.getOne(id);

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

