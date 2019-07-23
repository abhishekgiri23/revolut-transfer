package com.revolut.account.exception;

import com.revolut.common.errors.ApplicationException;

public class AccountNotCreatedException extends ApplicationException {
    
    public AccountNotCreatedException(Throwable cause) {
        super("Account Not Created",cause);
    }
    
    public AccountNotCreatedException() {
        super("Account Not Created");
    }
    
}
