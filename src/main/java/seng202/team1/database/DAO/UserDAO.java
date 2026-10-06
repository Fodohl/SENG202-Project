package seng202.team1.database.DAO;

import seng202.team1.database.DatabaseManager;
import seng202.team1.database.exceptions.DuplicateEntryException;
import seng202.team1.types.user.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;
import java.util.Locale;

/**
 * Database Access Object for user accounts
 * @author abdallaweza
 */
public class UserDAO extends BaseDAO<User> {

    /**
     * Instantiates UserDAO and gets a reference to the database singleton
     * @param databaseManager database singleton
     */
    public UserDAO(DatabaseManager databaseManager) {
        super(databaseManager);
    }

    @Override
    protected User fromRow(ResultSet rs) throws SQLException {
        int resetCode = rs.getInt("reset_code");
        Integer resetCodeOrNull = rs.wasNull() ? null : resetCode;
        return new User(
                rs.getInt("id"),
                getInstant(rs, "created_at"),
                getInstant(rs, "updated_at"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("password_hash"),
                resetCodeOrNull);
    }

    @Override
    public List<User> getAll() {
        return queryList("SELECT * FROM users");
    }

    @Override
    public User getOne(int id) {
        return queryOne("SELECT * FROM users WHERE id = ?", id);
    }

    /**
     * Finds a user by email (case-insensitive)
     * @param email email to look up
     * @return the user, or null if none
     */
    public User getByEmail(String email) {
        return queryOne("SELECT * FROM users WHERE email = ?", email.toLowerCase(Locale.ROOT));
    }

    /**
     * Adds a user. The passwordHash must already be hashed.
     * @return the new user's id, or -1 on SQL error
     * @throws DuplicateEntryException if the email is already registered
     */
    @Override
    public int add(User user) throws DuplicateEntryException {
        if (getByEmail(user.email()) != null) {
            throw new DuplicateEntryException("Email already registered: " + user.email());
        }
        String sql = "INSERT INTO users (created_at, updated_at, name, email, password_hash, reset_code) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        Instant now = Instant.now();
        return executeInsert(sql,
                now,
                now,
                user.name(),
                user.email().toLowerCase(Locale.ROOT),
                user.passwordHash(),
                user.resetCode());
    }

    @Override
    public void delete(int id) {
        deleteById("users", id);
    }

    @Override
    public void update(User user) {
        String sql = "UPDATE users SET updated_at = ?, name = ?, email = ?, password_hash = ?, reset_code = ? "
                + "WHERE id = ?";
        int rows = executeUpdate(sql,
                Instant.now(),
                user.name(),
                user.email().toLowerCase(Locale.ROOT),
                user.passwordHash(),
                user.resetCode(),
                user.id());
        if (rows == 0) {
            log.warn("Update affected 0 rows for user id {}", user.id());
        }
    }
}