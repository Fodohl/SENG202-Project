package seng202.team1.database.DAO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.database.DatabaseManager;
import seng202.team1.database.SqlTimeUtils;
import seng202.team1.types.bin.BinCategory;
import seng202.team1.types.bin.BinFullness;
import seng202.team1.types.status.ReportType;
import seng202.team1.types.trashlog.TrashType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Abstract base class for Database Access Objects. Holds the shared plumbing
 * (connection handling, parameter binding, row mapping) so each DAO only
 * defines its SQL and how a row becomes an object.
 *
 * @param <T> type of object this DAO manages
 */
public abstract class BaseDAO<T> extends SqlTimeUtils implements DAOInterface<T> {

    /// DatabaseManager managing all the DAO objects
    protected final DatabaseManager databaseManager;
    /// Logger named after the concrete DAO class
    protected final Logger log = LogManager.getLogger(getClass());

    /**
     * Instantiates the DAO with a reference to the database singleton
     * @param databaseManager database singleton
     */
    protected BaseDAO(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    /**
     * Converts the current row of a ResultSet into an object of type T
     * @param rs result set positioned on the row to convert
     * @return the converted object
     * @throws SQLException if a column cannot be read
     */
    protected abstract T fromRow(ResultSet rs) throws SQLException;

    /**
     * Binds parameters to a prepared statement. Instants go through
     * {@link #setInstant}, enums are stored via toString(), nulls are bound as SQL NULL.
     */
    private void bindParams(PreparedStatement ps, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            Object param = params[i];
            int index = i + 1;
            if (param == null) {
                ps.setNull(index, Types.NULL);
            } else if (param instanceof Instant instant) {
                setInstant(ps, index, instant);
            } else if (param instanceof Enum<?> enumValue) {
                ps.setString(index, enumValue.toString());
            } else {
                ps.setObject(index, param);
            }
        }
    }

    /**
     * Runs a SELECT and maps every row using {@link #fromRow}
     * @return list of results, empty on error
     */
    protected List<T> queryList(String sql, Object... params) {
        List<T> results = new ArrayList<>();
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            bindParams(ps, params);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(fromRow(rs));
                }
            }
        } catch (SQLException e) {
            log.error(e);
            return new ArrayList<>();
        }
        return results;
    }

    /**
     * Runs a SELECT and returns the first row
     * @return first result, or null if none or on error
     */
    protected T queryOne(String sql, Object... params) {
        List<T> results = queryList(sql, params);
        return results.isEmpty() ? null : results.get(0);
    }

    /**
     * Runs an INSERT, UPDATE or DELETE
     * @return number of rows affected, or -1 on SQL error
     */
    protected int executeUpdate(String sql, Object... params) {
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            bindParams(ps, params);
            return ps.executeUpdate();
        } catch (SQLException e) {
            log.error(e);
            return -1;
        }
    }

    /**
     * Runs an INSERT and returns the generated key
     * @return generated id, or -1 on SQL error
     */
    protected int executeInsert(String sql, Object... params) {
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            bindParams(ps, params);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : -1;
            }
        } catch (SQLException e) {
            log.error(e);
            return -1;
        }
    }

    /**
     * Deletes the row with the given id from the given table.
     * Table name must be a constant, never user input.
     */
    protected void deleteById(String table, int id) {
        executeUpdate("DELETE FROM " + table + " WHERE id = ?", id);
    }

    /**
     * Converts the string representation saved in the database to a {@link ReportType}
     * @param issueString String representation of report type
     * @return ReportType associated with the report
     */
    protected ReportType convertStringToIssue(String issueString) {
        try {
            return ReportType.valueOf(issueString.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Unexpected value: " + issueString, e);
        }
    }

    /**
     * Converts string from database to BinCategory
     * @param category string name
     * @return bin category that matches the string, GENERAL_WASTE if unknown
     */
    protected BinCategory convertStringToCategory(String category) {
        return switch (category.toUpperCase(Locale.ROOT)) {
            case "RECYCLABLE" -> BinCategory.RECYCLABLE;
            case "ORGANIC" -> BinCategory.ORGANIC;
            case "E-WASTE", "E_WASTE" -> BinCategory.E_WASTE;
            default -> BinCategory.GENERAL_WASTE;
        };
    }

    /**
     * Converts string from database to BinFullness
     * @param fullness string name
     * @return bin fullness that matches the string, UNKNOWN if null or unknown
     */
    protected BinFullness convertStringToFullness(String fullness) {
        if (fullness == null) {
            return BinFullness.UNKNOWN;
        }
        try {
            return BinFullness.valueOf(fullness.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            return BinFullness.UNKNOWN;
        }
    }

    /**
     * Converts string from database to TrashType
     *
     * @param type string name
     * @return trash type that matches the string, OTHER if null or unknown
     */
    protected TrashType convertStringToType(String type) {
        if (type == null) {
            return TrashType.OTHER;
        }
        try {
            return TrashType.valueOf(type.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            return TrashType.OTHER;
        }
    }
}