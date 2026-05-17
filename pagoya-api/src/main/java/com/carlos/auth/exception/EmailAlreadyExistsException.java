package com.carlos.auth.exception;

import com.carlos.shared.exception.BusinessRuleException;

public class EmailAlreadyExistsException extends BusinessRuleException {
    public EmailAlreadyExistsException(String email) {
        super("el email " + email + " ya esta registrado");
    }
}
