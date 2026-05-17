package com.carlos.account.exception;

import com.carlos.shared.exception.BusinessRuleException;

public class DuplicateAccountTypeException extends BusinessRuleException {
    public DuplicateAccountTypeException() {
        super("ya tiene una cuenta de este tipo");
    }
}