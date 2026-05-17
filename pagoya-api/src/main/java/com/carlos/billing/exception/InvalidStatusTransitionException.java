package com.carlos.billing.exception;
import com.carlos.shared.exception.BusinessRuleException;

public class InvalidStatusTransitionException extends BusinessRuleException {
    public InvalidStatusTransitionException(String message) {
        super(message);
    }
}