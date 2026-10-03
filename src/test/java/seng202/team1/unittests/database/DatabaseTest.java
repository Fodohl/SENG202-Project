package seng202.team1.unittests.database;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import seng202.team1.unittests.TrashAppTest;

import java.sql.Connection;
import java.sql.SQLException;

class DatabaseTest extends TrashAppTest {

    @Test
    void testDatabaseConnection() throws SQLException {
        Connection connection = databaseManager.connect();
        Assertions.assertNotNull(connection);
        Assertions.assertNotNull(connection.getMetaData());
        connection.close();
    }
}
