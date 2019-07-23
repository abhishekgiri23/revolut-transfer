package com.revolut.account.exception;

import com.revolut.common.errors.ApplicationException;

public class AccountException extends ApplicationException {
    
    
    public AccountException(String message) {
        super(message);
    }
    
    public AccountException(String message, Throwable cause) {
        super(message, cause);
    }
}
