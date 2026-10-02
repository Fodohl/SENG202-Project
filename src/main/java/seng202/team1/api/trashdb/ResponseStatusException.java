package seng202.team1.api.trashdb;

import seng202.team1.api.HttpStatus;

public class ResponseStatusException extends RuntimeException {

    public ResponseStatusException(HttpStatus status, String message) {
        super(String.format("%s - %s", status.toString(), message));
    }
}
