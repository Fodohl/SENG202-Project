package seng202.team1.unittests.data.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team1.types.user.User;
import seng202.team1.database.DAO.UserDAO;
import seng202.team1.database.exceptions.DuplicateEntryException;
import seng202.team1.unittests.TrashAppTest;

/**
 * Tests for UserDAO. TrashAppTest resets the test database before each test,
 * so every test starts with an empty users table.
 * @author abdallaweza
 */
public class UserDAOTest extends TrashAppTest {

    private UserDAO users;

    @BeforeEach
    void setUpUserDAO() {
        users = new UserDAO(databaseManager);
    }

    /** Builds a new (not yet saved) user with a hashed password */
    private User newUser(String name, String email, String password) {
        return new User(null, null, null, name, email, "", null);
    }

    @Test
    void testAddReturnsId() throws DuplicateEntryException {
        int id = users.add(newUser("Alice", "alice@test.com", "Password1!"));
        Assertions.assertTrue(id > 0);
        Assertions.assertEquals(1, users.getAll().size());
    }

    @Test
    void testGetOneReturnsSavedFields() throws DuplicateEntryException {
        int id = users.add(newUser("Alice", "alice@test.com", "Password1!"));
        User saved = users.getOne(id);

        Assertions.assertNotNull(saved);
        Assertions.assertEquals(id, saved.id());
        Assertions.assertEquals("Alice", saved.name());
        Assertions.assertEquals("alice@test.com", saved.email());
        Assertions.assertNotNull(saved.created());
        Assertions.assertNotNull(saved.updated());
        Assertions.assertNull(saved.resetCode());
    }

    @Test
    void testGetOneUnknownIdReturnsNull() {
        Assertions.assertNull(users.getOne(999));
    }


    @Test
    void testGetByEmailIsCaseInsensitive() throws DuplicateEntryException {
        users.add(newUser("Alice", "Alice@Test.com", "Password1!"));

        Assertions.assertNotNull(users.getByEmail("alice@test.com"));
        Assertions.assertNotNull(users.getByEmail("ALICE@TEST.COM"));
    }

    @Test
    void testGetByEmailUnknownReturnsNull() {
        Assertions.assertNull(users.getByEmail("nobody@test.com"));
    }

    @Test
    void testDuplicateEmailThrows() throws DuplicateEntryException {
        users.add(newUser("Alice", "alice@test.com", "Password1!"));

        Assertions.assertThrows(DuplicateEntryException.class,
                () -> users.add(newUser("Alice2", "ALICE@test.com", "Other1!")));
        Assertions.assertEquals(1, users.getAll().size());
    }

    @Test
    void testGetAllReturnsEveryUser() throws DuplicateEntryException {
        users.add(newUser("Alice", "alice@test.com", "Password1!"));
        users.add(newUser("Bob", "bob@test.com", "Password1!"));

        Assertions.assertEquals(2, users.getAll().size());
    }

    @Test
    void testUpdateChangesName() throws DuplicateEntryException {
        int id = users.add(newUser("Alice", "alice@test.com", "Password1!"));
        User before = users.getOne(id);

        User changed = new User(before.id(), before.created(), before.updated(),
                "Alicia", before.email(), before.passwordHash(), before.resetCode());
        users.update(changed);

        User after = users.getOne(id);
        Assertions.assertEquals("Alicia", after.name());
        Assertions.assertEquals(before.email(), after.email());
        Assertions.assertEquals(before.passwordHash(), after.passwordHash());
    }

    @Test
    void testUpdateStoresResetCode() throws DuplicateEntryException {
        int id = users.add(newUser("Alice", "alice@test.com", "Password1!"));
        User before = users.getOne(id);

        users.update(new User(before.id(), before.created(), before.updated(),
                before.name(), before.email(), before.passwordHash(), 123456));

        Assertions.assertEquals(123456, users.getOne(id).resetCode());
    }

    @Test
    void testDeleteRemovesUser() throws DuplicateEntryException {
        int id = users.add(newUser("Alice", "alice@test.com", "Password1!"));
        users.delete(id);

        Assertions.assertNull(users.getOne(id));
        Assertions.assertEquals(0, users.getAll().size());
    }
}
