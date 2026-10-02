package seng202.team1.api;

import seng202.team1.helpers.StringHelpers;

public enum HttpStatus {

    UNAUTHORIZED(401),
    NOT_FOUNT(404),
    CONFLICT(409),

    INTERNAL_SERVER_ERROR(500);

    private Integer code;

    private HttpStatus(Integer code){
        this.code = code;
    }

    @Override
    public String toString() {

        return String.format("%s (%s)", StringHelpers.enumNameToTitle(name()), code);

    }
}
