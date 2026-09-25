package seng202.team1.data;

/**
 * User
 * Class represents a single user
 */

public class User {

    private final int userID;
    private final String name;
    private final String email;

    /**
     * Constructor for class User
     * @param userID
     * @param name
     */
    public User(int userID, String name, String email) {
        this.userID = userID;
        this.name = name;
        this.email = email;
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

    public String getEmail() {
        return email;
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
