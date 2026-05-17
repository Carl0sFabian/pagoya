package com.carlos.billing.exception;
import com.carlos.shared.exception.BusinessRuleException;

public class InvalidSchedulingException extends BusinessRuleException {
    public InvalidSchedulingException(String message) {
        super(message);
    }
}