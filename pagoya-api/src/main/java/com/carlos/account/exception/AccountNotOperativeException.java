package com.carlos.account.exception;

public class AccountNotOperativeException extends RuntimeException {

    public AccountNotOperativeException() {
        super("La cuenta origen no se encuentra activa u operativa");
    }
    public AccountNotOperativeException(String message) {
        super(message);
    }
}