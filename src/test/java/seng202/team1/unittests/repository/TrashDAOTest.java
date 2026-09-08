package seng202.team1.unittests.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team1.data.Trash;
import seng202.team1.data.TrashCategory;
import seng202.team1.data.TrashType;
import seng202.team1.exceptions.InstanceAlreadyExistsException;
import seng202.team1.repository.DatabaseManager;
import seng202.team1.repository.TrashDAO;

public class TrashDAOTest {

    static TrashDAO trashDAO;
    static DatabaseManager databaseManager;

    @BeforeAll
    static void setup() throws InstanceAlreadyExistsException {
        DatabaseManager.REMOVE_INSTANCE();
        databaseManager = DatabaseManager.initialiseInstanceWithUrl("jdbc:sqlite:./src/test/resources/test_database.db");
        trashDAO = new TrashDAO();
    }

    @BeforeEach
    void resetDB() {
        databaseManager.resetDB();
    }

    @Test
    void testAddTrash() {
        Trash trashToAdd = new Trash(TrashCategory.RECYCLABLE, TrashType.PAPER, 20);
        trashDAO.add(trashToAdd);
        Assertions.assertEquals(1, trashDAO.getAll().size());
        Trash trash = trashDAO.getAll().get(0);
        Assertions.assertEquals(trashToAdd.trashCategory(), trash.trashCategory());
        Assertions.assertEquals(trashToAdd.trashType(), trash.trashType());
        Assertions.assertEquals(trashToAdd.trashWeight(), trash.trashWeight());
    }

    @Test
    void testGetTrashByID() {
        Trash trashToAdd = new Trash(TrashCategory.RECYCLABLE, TrashType.PAPER, 20);
        int id = trashDAO.add(trashToAdd);
        Trash returnedTrash = trashDAO.getOne(id);
        Assertions.assertEquals(trashToAdd.trashCategory(), returnedTrash.trashCategory());
        Assertions.assertEquals(trashToAdd.trashType(), returnedTrash.trashType());
        Assertions.assertEquals(trashToAdd.trashWeight(), returnedTrash.trashWeight());
    }

    @Test
    void testDeleteSale() {
        int insertId  = trashDAO.add(new Trash(TrashCategory.RECYCLABLE, TrashType.PAPER, 20));
        int totalSalesBefore = trashDAO.getAll().size();
        trashDAO.delete(insertId);
        Assertions.assertEquals(totalSalesBefore - 1, trashDAO.getAll().size());
    }

    @Test
    void testUpdateTrash() {
        Trash trash  = new Trash(TrashCategory.RECYCLABLE, TrashType.PAPER, 20);
        int id = trashDAO.add(trash);
        Trash updatedTrash = new Trash(id, TrashCategory.ORGANIC, TrashType.OTHER, 100);
        trashDAO.update(updatedTrash);

        Trash result = trashDAO.getOne(id); // or whatever your read method is called

        Assertions.assertEquals(updatedTrash.trashCategory(), result.trashCategory());
        Assertions.assertEquals(updatedTrash.trashType(), result.trashType());
        Assertions.assertEquals(updatedTrash.trashWeight(), result.trashWeight());
    }

}

