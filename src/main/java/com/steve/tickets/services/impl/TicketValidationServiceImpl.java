package com.steve.tickets.services.impl;

import com.steve.tickets.Exceptions.QrCodeNotFoundException;
import com.steve.tickets.Exceptions.TicketNotFoundException;
import com.steve.tickets.domain.entities.*;
import com.steve.tickets.repositories.QrCodeRepository;
import com.steve.tickets.repositories.TicketRepository;
import com.steve.tickets.repositories.TicketValidationRepository;
import com.steve.tickets.services.TicketValidationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketValidationServiceImpl implements TicketValidationService {
    private final QrCodeRepository qrCodeRepository;
    private final TicketValidationRepository ticketValidationRepository;
    private final TicketRepository ticketRepository;

    @Override
    public TicketValidation validateTicketByQrCode(UUID qrCodeId) {
        QrCode qrCode = qrCodeRepository.findByIdAndStatus(qrCodeId, QrCodeStatusEnum.ACTIVE).orElseThrow(() -> new QrCodeNotFoundException(String.format("Qr code with id '%s' not found", qrCodeId)));
        Ticket ticket = qrCode.getTicket();

        return validateTicket(ticket, TicketValidationMethod.QR_SCAN);

    }


    @Override
    public TicketValidation validateTicketManually(UUID ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(TicketNotFoundException::new);
        return validateTicket(ticket, TicketValidationMethod.MANUAL);
    }

    private TicketValidation validateTicket(Ticket ticket,TicketValidationMethod ticketValidationMethod) {
        TicketValidation ticketValidation = new TicketValidation();
        ticketValidation.setTicket(ticket);
        ticketValidation.setValidationMethod(TicketValidationMethod.QR_SCAN);

        TicketValidationStatusEnum ticketValidationStatus = ticket.getValidations().stream().filter(v -> TicketValidationStatusEnum.VALID.equals(v.getStatus())).findFirst().map(v -> TicketValidationStatusEnum.INVALID).orElse(TicketValidationStatusEnum.VALID);

        ticketValidation.setStatus(ticketValidationStatus);
        return ticketValidationRepository.save(ticketValidation);
    }
}
