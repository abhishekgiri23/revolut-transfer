package com.revolut.common.errors;

import java.util.Objects;

public class ApplicationException extends RuntimeException {
    
    protected ApplicationException(String message) {
        super(message);
    }
    
    protected ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
    
    protected ApplicationException(Throwable cause) {
        super(cause);
    }
    
}
