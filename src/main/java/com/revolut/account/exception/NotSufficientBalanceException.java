package com.revolut.account.exception;

import com.revolut.common.errors.ApplicationException;

public class NotSufficientBalanceException extends ApplicationException {
    
    public NotSufficientBalanceException(Throwable cause) {
        super("Not Sufficient Balance", cause);
    }
    
    public NotSufficientBalanceException(String errorMessage) {
        super(errorMessage);
    }
    
    public NotSufficientBalanceException() {
        super("Not Sufficient Balance");
    }
    
}
