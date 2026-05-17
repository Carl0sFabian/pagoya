package com.carlos.transfer.exception;

import com.carlos.shared.exception.BusinessRuleException;

public class InsufficientBalanceException extends BusinessRuleException {
    public InsufficientBalanceException() { super("saldo insuficiente"); }
}
