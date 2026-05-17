package com.carlos.billing.exception;

import com.carlos.shared.exception.BusinessRuleException;

public class InactiveProviderException extends BusinessRuleException {
    public InactiveProviderException() {
        super("el proveedor seleccionado no esta disponible");
    }
}
