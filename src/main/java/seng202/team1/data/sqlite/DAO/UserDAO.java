package seng202.team1.data.sqlite.DAO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.api.trashdb.LocalUser;
import seng202.team1.data.sqlite.DatabaseManager;
import seng202.team1.data.sqlite.SqlTimeUtils;
import seng202.team1.data.sqlite.exceptions.DuplicateEntryException;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Database Access Object for user accounts
 * @author abdallaweza
 */
public class UserDAO extends SqlTimeUtils implements DAOInterface<LocalUser> {
    private static final Logger log = LogManager.getLogger(UserDAO.class);
    private final DatabaseManager databaseManager;

    public UserDAO(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    /** Converts the current row of a ResultSet into a LocalUser */
    private LocalUser fromRow(ResultSet rs) throws SQLException {
        int resetCode = rs.getInt("reset_code");
        Integer resetCodeOrNull = rs.wasNull() ? null : resetCode;
        return new LocalUser(
                rs.getInt("id"),
                getInstant(rs, "created_at"),
                getInstant(rs, "updated_at"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("password_hash"),
                resetCodeOrNull);
    }

    /** Sets an Integer that may be null */
    private void setNullableInt(PreparedStatement ps, int index, Integer value) throws SQLException {
        if (value == null) {
            ps.setNull(index, Types.INTEGER);
        } else {
            ps.setInt(index, value);
        }
    }

    @Override
    public List<LocalUser> getAll() {
        List<LocalUser> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection conn = databaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                users.add(fromRow(rs));
            }
        } catch (SQLException e) {
            log.error(e);
        }
        return users;
    }

    @Override
    public LocalUser getOne(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? fromRow(rs) : null;
            }
        } catch (SQLException e) {
            log.error(e);
            return null;
        }
    }

    /**
     * Finds a user by email (case-insensitive)
     * @return the user, or null if none
     */
    public LocalUser getByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email.toLowerCase(Locale.ROOT));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? fromRow(rs) : null;
            }
        } catch (SQLException e) {
            log.error(e);
            return null;
        }
    }

    /**
     * Adds a user. The passwordHash must already be hashed.
     * @return the new user's id, or -1 on SQL error
     * @throws DuplicateEntryException if the email is already registered
     */
    @Override
    public int add(LocalUser user) throws DuplicateEntryException {
        if (getByEmail(user.email()) != null) {
            throw new DuplicateEntryException("Email already registered: " + user.email());
        }
        String sql = "INSERT INTO users (created_at, updated_at, name, email, password_hash, reset_code) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            Instant now = Instant.now();
            setInstant(ps, 1, now);
            setInstant(ps, 2, now);
            ps.setString(3, user.name());
            ps.setString(4, user.email().toLowerCase(Locale.ROOT));
            ps.setString(5, user.passwordHash());
            setNullableInt(ps, 6, user.resetCode());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : -1;
            }
        } catch (SQLException e) {
            log.error(e);
            return -1;
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error(e);
        }
    }

    @Override
    public void update(LocalUser user) {
        String sql = "UPDATE users SET updated_at = ?, name = ?, email = ?, password_hash = ?, reset_code = ? "
                + "WHERE id = ?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            setInstant(ps, 1, Instant.now());
            ps.setString(2, user.name());
            ps.setString(3, user.email().toLowerCase(Locale.ROOT));
            ps.setString(4, user.passwordHash());
            setNullableInt(ps, 5, user.resetCode());
            ps.setInt(6, user._id());
            if (ps.executeUpdate() == 0) {
                log.warn("Update affected 0 rows for user id {}", user._id());
            }
        } catch (SQLException e) {
            log.error(e);
        }
    }
}