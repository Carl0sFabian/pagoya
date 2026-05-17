package com.carlos.customer.exception;

import com.carlos.shared.exception.BusinessRuleException;

public class CustomerProfileAlreadyExistsException extends BusinessRuleException {
    public CustomerProfileAlreadyExistsException() {
        super("el usuario ya tiene un perfil de cliente");
    }
}