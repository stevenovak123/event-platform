package com.steve.tickets.services.impl;

import com.steve.tickets.Exceptions.TicketSoldOutException;
import com.steve.tickets.Exceptions.TicketTypeNotFoundException;
import com.steve.tickets.Exceptions.UserNotFoundException;
import com.steve.tickets.domain.entities.Ticket;
import com.steve.tickets.domain.entities.TicketStatusEnum;
import com.steve.tickets.domain.entities.TicketType;
import com.steve.tickets.domain.entities.User;
import com.steve.tickets.repositories.TicketRepository;
import com.steve.tickets.repositories.TicketTypeRepository;
import com.steve.tickets.repositories.UserRepository;
import com.steve.tickets.services.QrCodeService;
import com.steve.tickets.services.TicketTypeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketTypeServiceImpl implements TicketTypeService {

    private final UserRepository userRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final TicketRepository ticketRepository;
    private final QrCodeService qrCodeService;


    @Override
    @Transactional
    public Ticket purchaseTicket(UUID userId, UUID ticketTypeId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(String.format("User with ID '%s' not found", userId)));

        TicketType ticketType = ticketTypeRepository.findByIdWithLock(ticketTypeId).orElseThrow(() -> new TicketTypeNotFoundException(String.format("TicketType with ID '%s' not found", ticketTypeId)));

        int purchasedTickets = ticketRepository.countByTicketTypeId(ticketType.getId());

        Integer totalAvailable = ticketType.getTotalAvailable();

        if (purchasedTickets + 1 > totalAvailable) {
            throw new TicketSoldOutException();
        }

        Ticket ticket = new Ticket();
        ticket.setStatus(TicketStatusEnum.PURCHASED);
        ticket.setTicketType(ticketType);
        ticket.setPurchaser(user);

        Ticket savedTicket = ticketRepository.save(ticket);
        qrCodeService.generateQRCode(savedTicket);

        return ticketRepository.save(savedTicket);

    }
}
