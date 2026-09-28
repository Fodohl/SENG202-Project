package seng202.team1.endpoints.api.v1;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.View;
import seng202.team1.taserver.model.ErrorResponse;

@RestControllerAdvice
public class Errors {

    /// UImage specific logger
    private static final Logger log = LogManager.getLogger(Errors.class);

    @ExceptionHandler(ResponseStatusException.class)

    public ResponseEntity<ErrorResponse> handleResponseStatusException(ResponseStatusException error) {

        log.warn("API error ({}) {}", error.getStatusCode(), error.getReason());

        ErrorResponse errorBody = new ErrorResponse();
        errorBody.setError(error.getReason());

        return ResponseEntity.status(error.getStatusCode()).body(errorBody);
    }
}