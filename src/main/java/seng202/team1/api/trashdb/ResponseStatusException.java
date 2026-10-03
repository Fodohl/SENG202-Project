package seng202.team1.api.trashdb;

import seng202.team1.api.HttpStatus;

/**
 * Exception for an API call failing with a given HTTP code
 *
 * Note: minimal representation of spring framework's ResponseStatusException, to simulate API
 */
public class ResponseStatusException extends RuntimeException {

    /// The error code for this type of exception
    private HttpStatus status;

    /// The reason this exception occurred
    private String message;

    /**
     * Constructs a new ResponseStatusException
     *
     * @param status The error code
     * @param message The error reason
     */
    public ResponseStatusException(HttpStatus status, String message) {
        super(String.format("%s - %s", status.toString(), message));
    }

    /**
     * Get the reason this error happened
     *
     * @return The reason the error occurred
     */
    public String getResponseBody() {
        return message;
    }

    /**
     * Get the error code this exception this represents
     *
     * @return The error code
     */
    public Integer getCode() {
        return status.getCode();
    }
}
