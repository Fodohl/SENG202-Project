package seng202.team1.unittests.data.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import seng202.team1.data.repository.DatabaseManager;

import java.sql.Connection;
import java.sql.SQLException;

class DatabaseTest {

    @Test
    void testDatabaseManagerInstance() {
        DatabaseManager databaseManager = DatabaseManager.getInstance();
        Assertions.assertNotNull(databaseManager);
        Assertions.assertEquals(databaseManager, DatabaseManager.getInstance());
    }

    @Test
    void testDatabaseConnection() throws SQLException {
        Connection connection = DatabaseManager.getInstance().connect();
        Assertions.assertNotNull(connection);
        Assertions.assertNotNull(connection.getMetaData());
        connection.close();
    }
}
