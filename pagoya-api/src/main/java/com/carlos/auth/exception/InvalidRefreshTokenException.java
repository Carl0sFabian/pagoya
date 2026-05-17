package com.carlos.auth.exception;

import com.carlos.shared.exception.BusinessRuleException;

public class InvalidRefreshTokenException extends BusinessRuleException {
    public InvalidRefreshTokenException() {
        super("refresh token invalido, expirado o revocado");
    }
}
