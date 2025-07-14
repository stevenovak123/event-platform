package com.steve.tickets.mappers;

import com.steve.tickets.domain.dtos.ListTicketResponseDto;
import com.steve.tickets.domain.dtos.ListTicketTicketTypeResponseDto;
import com.steve.tickets.domain.entities.Ticket;
import com.steve.tickets.domain.entities.TicketType;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TicketMapper {
    ListTicketTicketTypeResponseDto toListTicketTicketTypeResponseDto(TicketType ticketType);
    ListTicketResponseDto toListTicketResponseDto(Ticket ticket);
}
