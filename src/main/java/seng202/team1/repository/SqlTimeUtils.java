package seng202.team1.repository;

import java.sql.*;
import java.time.Instant;

/**
 * Helper class for DAOs to convert milliseconds to Instant and vice versa
 * @author Marcus Demafeliz
 */
public class SqlTimeUtils {

    SqlTimeUtils() {}

    /**
     * Retrieves a timestamp from a result set and converts it to an Instant.
     *
     * @param rs the result set containing the timestamp
     * @param column the name of the timestamp column
     * @return the timestamp as an Instant, or null if the database value is null
     * @throws SQLException if an error occurs while accessing the result set
     */
    protected Instant getInstant(ResultSet rs, String column)
            throws SQLException {
        long millis = rs.getLong(column);
        return rs.wasNull() ? null : Instant.ofEpochMilli(millis);
    }

    /**
     * Stores an Instant in a prepared statement as milliseconds since the Unix epoch.
     *
     * @param ps the prepared statement
     * @param index the parameter index in the prepared statement
     * @param instant the Instant to store, or null
     * @throws SQLException if an error occurs while setting the parameter
     */
    protected void setInstant(PreparedStatement ps, int index, Instant instant)
            throws SQLException {
        if (instant == null) {
            ps.setNull(index, Types.BIGINT);
        } else {
            ps.setLong(index, instant.toEpochMilli());
        }
    }
}