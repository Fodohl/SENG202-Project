package seng202.team1.models;

/**
 * User
 * Class represents a single user
 */

public class User {
    private final int userID;
    private final String name;

    /**
     * Constructor for class User
     * @param userID
     * @param name
     */

    public User(int userID, String name) {
        this.userID = userID;
        this.name = name;
    }

    /**
     * Get the userID
     * Return the userID
     */

    public int getUserID() {
        return userID;
    }

    /**
     * Get the name
     * Return the name
     */

    public String getName() {
        return name;
    }

    /**
     * String toString
     * @return name
     */

    @Override
    public String toString() {
        return name;
    }
}
