package com.carlos.auth.exception;

public class EmailAlreadyExistsException extends BusinessRuleException {
    public EmailAlreadyExistsException(String email) {
        super("el email " + email + " ya esta registrado");
    }
}
