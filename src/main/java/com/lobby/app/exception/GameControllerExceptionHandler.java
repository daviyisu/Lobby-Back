package com.lobby.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

@ControllerAdvice
public class GameControllerExceptionHandler {

    @ExceptionHandler(SteamPrivateAccountException.class)
    public ResponseEntity<Object> steamPrivateAccountException(SteamPrivateAccountException e) {
        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.WARNING, e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Steam account is private or does not have games");
    }
}
