package seng202.team1.data;

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
     * Gets the userID
     * @return the userID
     */

    public int getUserID() {
        return userID;
    }

    /**
     * Gets the name
     * @return the name
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
