package com.steve.tickets.services;

import com.steve.tickets.domain.entities.TicketValidation;
import org.springframework.stereotype.Service;

import java.util.UUID;


public  interface TicketValidationService {
    TicketValidation validateTicketByQrCode(UUID qrCodeId);
    TicketValidation validateTicketManually (UUID ticketId);
}
