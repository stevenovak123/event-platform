package com.steve.tickets.Exceptions;

public class EventUpdateException extends EventTicketException{
    public EventUpdateException() {
    }

    public EventUpdateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public EventUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public EventUpdateException(String message) {
        super(message);
    }

    public EventUpdateException(Throwable cause) {
        super(cause);
    }
}
