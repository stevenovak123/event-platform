package com.steve.tickets.services.impl;

import com.steve.tickets.Exceptions.EventNotFoundException;
import com.steve.tickets.Exceptions.EventUpdateException;
import com.steve.tickets.Exceptions.TicketTypeNotFoundException;
import com.steve.tickets.Exceptions.UserNotFoundException;
import com.steve.tickets.domain.CreateEventRequest;
import com.steve.tickets.domain.UpdateEventRequest;
import com.steve.tickets.domain.UpdateTicketTypeRequest;
import com.steve.tickets.domain.entities.Event;
import com.steve.tickets.domain.entities.TicketType;
import com.steve.tickets.domain.entities.User;
import com.steve.tickets.repositories.EventRepository;
import com.steve.tickets.repositories.UserRepository;
import com.steve.tickets.services.EventService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;


    @Override
    @Transactional
    public Event createEvent(UUID organiserID, CreateEventRequest eventRequest) {
        User organiser = userRepository.findById(organiserID).orElseThrow(() -> new UserNotFoundException(String.format("User with ID '%s' is not found", organiserID)));
        Event eventToCreate = new Event();
        List<TicketType> ticketTypesToCreate = eventRequest.getTicketTypes().stream().map(ticketType -> {
            TicketType ticketTypeToCreate = new TicketType();
            ticketTypeToCreate.setName(ticketType.getName());
            ticketTypeToCreate.setPrice(ticketType.getPrice());
            ticketTypeToCreate.setDescription(ticketType.getDescription());
            ticketTypeToCreate.setTotalAvailable(ticketType.getTotalAvailable());
            ticketTypeToCreate.setEvent(eventToCreate);
            return ticketTypeToCreate;
        }).toList();

        eventToCreate.setName(eventRequest.getName());
        eventToCreate.setStart(eventRequest.getStart());
        eventToCreate.setEnd(eventRequest.getEnd());
        eventToCreate.setVenue(eventRequest.getVenue());
        eventToCreate.setSalesStart(eventRequest.getSalesStart());
        eventToCreate.setSalesEnd(eventRequest.getSalesEnd());
        eventToCreate.setOrganizer(organiser);
        eventToCreate.setTicketTypes(ticketTypesToCreate);
        eventToCreate.setStatus(eventRequest.getStatus());
        return eventRepository.save(eventToCreate);
    }

    @Override
    public Page<Event> listEventForOrganizer(UUID organizerId, Pageable pageable) {
        return eventRepository.findByOrganizerId(organizerId, pageable);
    }

    @Override
    public Optional<Event> getEventForOrganizer(UUID organizerId, UUID id) {
        return eventRepository.findByIdAndOrganizerId(id, organizerId);
    }

    @Override
    @Transactional
    public Event updateEventForOrganizer(UUID organizerId, UUID id, UpdateEventRequest event) {
        if (event.getId() == null) {
            throw new EventUpdateException("Event ID cannot be null");
        }
        if (!id.equals(event.getId())) {
            throw new EventUpdateException("Cannot Update ID of an Event");
        }
        Event existingEvent = eventRepository.findByIdAndOrganizerId(id, organizerId).orElseThrow(() -> new EventNotFoundException(String.format("Event with ID '%s' does not exists", id)));
        existingEvent.setName(event.getName());
        existingEvent.setStart(event.getStart());
        existingEvent.setEnd(event.getEnd());
        existingEvent.setVenue(event.getVenue());
        existingEvent.setSalesStart(event.getSalesStart());
        existingEvent.setSalesEnd(event.getSalesEnd());
        existingEvent.setStatus(event.getStatus());
        Set<UUID> requestTicketTypeIds = event.getTicketTypes().stream()
                .map(UpdateTicketTypeRequest::getId).filter(Objects::nonNull).collect(Collectors.toSet());
        existingEvent.getTicketTypes().removeIf(existingTicketType -> !requestTicketTypeIds.contains(existingTicketType.getId()));

        Map<UUID, TicketType> existingTicketTypeIndex = existingEvent.getTicketTypes().stream().collect(Collectors.toMap(TicketType::getId, Function.identity()));
        for (UpdateTicketTypeRequest ticketType: event.getTicketTypes()) {
            if (null == ticketType.getId()) {
                // create
                TicketType ticketTypeToCreate = new TicketType();
                ticketTypeToCreate.setName(ticketType.getName());
                ticketTypeToCreate.setPrice(ticketType.getPrice());
                ticketTypeToCreate.setDescription(ticketType.getDescription());
                ticketTypeToCreate.setTotalAvailable(ticketType.getTotalAvailable());
                ticketTypeToCreate.setEvent(existingEvent);
                existingEvent.getTicketTypes().add(ticketTypeToCreate);
            } else if (existingTicketTypeIndex.containsKey(ticketType.getId())) {
                //update
                TicketType existingTicketType = existingTicketTypeIndex.get(ticketType.getId());
                existingTicketType.setName(ticketType.getName());
                existingTicketType.setPrice(ticketType.getPrice());
                existingTicketType.setDescription(ticketType.getDescription());
                existingTicketType.setTotalAvailable(ticketType.getTotalAvailable());
            } else {
                throw new TicketTypeNotFoundException(String.format("Ticket Type of '%s' not found", ticketType.getId()));
            }
        }
        return eventRepository.save(existingEvent);

    }
}
