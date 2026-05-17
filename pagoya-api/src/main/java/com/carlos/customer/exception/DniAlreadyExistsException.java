package com.carlos.customer.exception;

import com.carlos.shared.exception.BusinessRuleException;

public class DniAlreadyExistsException extends BusinessRuleException {
    public DniAlreadyExistsException(String dni) {
        super("el DNI " + dni + " ya esta registrado");
    }
}