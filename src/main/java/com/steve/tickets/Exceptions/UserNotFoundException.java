package com.steve.tickets.Exceptions;

public class UserNotFoundException extends EventTicketException{
    public UserNotFoundException() {
    }

    public UserNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(Throwable cause) {
        super(cause);
    }
}
