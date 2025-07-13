package com.steve.tickets.Exceptions;

public class TicketTypeNotFoundException extends EventTicketException{
    public TicketTypeNotFoundException() {
    }

    public TicketTypeNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public TicketTypeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public TicketTypeNotFoundException(String message) {
        super(message);
    }

    public TicketTypeNotFoundException(Throwable cause) {
        super(cause);
    }
}
