package com.carlos.transfer.exception;

import com.carlos.shared.exception.BusinessRuleException;

public class SameAccountTransferException extends BusinessRuleException {
    public SameAccountTransferException() {
        super("la cuenta origen y destino no pueden ser la misma");
    }
}