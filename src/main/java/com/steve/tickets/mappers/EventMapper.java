package com.steve.tickets.mappers;

import com.steve.tickets.domain.CreateEventRequest;
import com.steve.tickets.domain.CreateTicketTypeRequest;
import com.steve.tickets.domain.UpdateEventRequest;
import com.steve.tickets.domain.UpdateTicketTypeRequest;
import com.steve.tickets.domain.dtos.*;
import com.steve.tickets.domain.entities.Event;
import com.steve.tickets.domain.entities.TicketType;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {

    CreateTicketTypeRequest fromDto(CreateTicketTypeRequestDto dto);

    CreateEventRequest fromDto(CreateEventRequestDto dto);

    CreateEventResponseDto toDto(Event event);

    ListEventTicketTypeResponseDto toDto(TicketType ticketType);

    ListEventResponseDto toListEventResponseDto(Event event);

    GetEventDetailsTicketTypesResponseDto
    toGetEventDetailsTicketTypesResponseDto(TicketType ticketType);

    GetEventDetailsResponseDto toGetEventDetailsResponseDto(Event event);

    UpdateTicketTypeRequest fromDto(UpdateTicketTypeRequestDto dto);

    UpdateEventRequest fromDto(UpdateEventRequestDto dto);

    UpdateTicketTypeRequestDto toUpdateTicketTypeRequestDto(TicketType ticketType);

    UpdateEventResponseDto toUpdateEventResponseDto(Event event);

    ListPublishedEventsResponseDto toListPublishedEventsResponseDto(Event event);

}
