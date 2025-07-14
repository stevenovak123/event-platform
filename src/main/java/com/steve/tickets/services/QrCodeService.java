package com.steve.tickets.services;

import com.steve.tickets.domain.entities.QrCode;
import com.steve.tickets.domain.entities.Ticket;

public interface QrCodeService {
    QrCode generateQRCode(Ticket ticket);
}
