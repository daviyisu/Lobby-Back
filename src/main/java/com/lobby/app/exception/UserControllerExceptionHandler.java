package com.lobby.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.util.logging.Level;
import java.util.logging.Logger;


@ControllerAdvice
public class UserControllerExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> handleException(ResponseStatusException e) {
        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.INFO, e.getReason(), e);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("User not found");
    }
}
