package com.steve.tickets.services;

import com.steve.tickets.domain.entities.QrCode;
import com.steve.tickets.domain.entities.Ticket;

import java.util.UUID;

public interface QrCodeService {
    QrCode generateQRCode(Ticket ticket);
    byte[] getQrCodeImageForUserAndTicket(UUID userId, UUID ticketId);
}
