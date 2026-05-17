package com.carlos.account.exception;

import com.carlos.shared.exception.BusinessRuleException;

public class AccountNotOperativeException extends BusinessRuleException {
    
    public AccountNotOperativeException() {
        super("la cuenta origen no esta operativa");
    }

    public AccountNotOperativeException(String message) {
        super(message);
    }
}
