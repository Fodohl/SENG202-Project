package seng202.team1.database;

import seng202.team1.database.DAO.*;

public class TableCollection {

    /// The database connection to the tables
    private DatabaseManager databaseManager;

    /// The DAO responsible for bins
    private BinDAO binTable;

    /// The DAO responsible for users
    private UserDAO userTable;

    /// The DAO responsible for trash
    private TrashLogDAO trashTable;

    /// The DAO responsible for status reports
    private StatusReportDAO statusReportsTable;

    /// The DAO responsible for community cleanliness
    private CommunityCleanlinessDAO cleanlinessTable;

    /// The DAO responsible for the leaderboard
    private LeaderboardDAO leaderboardTable;

    public TableCollection(DatabaseManager db) {

        databaseManager = db;
        binTable = new BinDAO(db);
        userTable = new UserDAO(db);
        trashTable = new TrashLogDAO(db);
        statusReportsTable = new StatusReportDAO(db);
        cleanlinessTable = new CommunityCleanlinessDAO(db);
        leaderboardTable = new LeaderboardDAO(db);
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public BinDAO getBinTable() {
        return binTable;
    }

    public UserDAO getUserTable() {
        return userTable;
    }

    public TrashLogDAO getTrashTable() {
        return trashTable;
    }

    public StatusReportDAO getStatusReportsTable() {
        return statusReportsTable;
    }

    public CommunityCleanlinessDAO getCleanlinessTable() {
        return cleanlinessTable;
    }

    public LeaderboardDAO getLeaderboardTable() {
        return leaderboardTable;
    }
}
