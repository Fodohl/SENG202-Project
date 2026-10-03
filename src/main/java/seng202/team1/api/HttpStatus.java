package seng202.team1.api;

import seng202.team1.helpers.StringHelpers;

/**
 * Represents a HTTP repsonse status code
 */
public enum HttpStatus {

    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    NOT_FOUNT(404),
    CONFLICT(409),

    INTERNAL_SERVER_ERROR(500);

    /// The response code
    private Integer code;

    /**
     * Constructs a new HttpStatus
     * @param code The error code
     */
    private HttpStatus(Integer code){
        this.code = code;
    }

    /**
     * Convert the enum to a string
     *
     * @return The value in the format 'message (code)'
     */
    @Override
    public String toString() {

        return String.format("%s (%s)", StringHelpers.enumNameToTitle(name()), code);

    }

    /**
     * Get the code of this status
     *
     * @return The status code
     */
    public Integer getCode() {
        return code;
    }
}
