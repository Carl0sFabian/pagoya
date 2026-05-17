package com.carlos.billing.exception;

import com.carlos.shared.exception.BusinessRuleException;

public class DuplicateBillPaymentException extends BusinessRuleException {
    public DuplicateBillPaymentException() {
        super("ya tienes registrado un pago para este recibo");
    }
}