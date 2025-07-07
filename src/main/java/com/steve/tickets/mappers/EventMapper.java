package com.steve.tickets.mappers;

import com.steve.tickets.domain.CreateEventRequest;
import com.steve.tickets.domain.CreateTicketTypeRequest;
import com.steve.tickets.domain.dtos.CreateEventRequestDto;
import com.steve.tickets.domain.dtos.CreateEventResponseDto;
import com.steve.tickets.domain.dtos.CreateTicketTypeRequestDto;
import com.steve.tickets.domain.entities.Event;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "string", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {

    CreateTicketTypeRequest fromDto(CreateTicketTypeRequestDto dto);

    CreateEventRequest fromDto(CreateEventRequestDto dto);

    CreateEventResponseDto toDto(Event event);
}
