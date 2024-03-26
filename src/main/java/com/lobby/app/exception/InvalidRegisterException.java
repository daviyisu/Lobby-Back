package com.lobby.app.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidRegisterException extends AuthenticationException {
    public InvalidRegisterException(String msg) {
        super(msg);
    }
}