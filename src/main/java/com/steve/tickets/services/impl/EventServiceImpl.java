package com.steve.tickets.services.impl;

import com.steve.tickets.Exceptions.UserNotFoundException;
import com.steve.tickets.domain.CreateEventRequest;
import com.steve.tickets.domain.entities.Event;
import com.steve.tickets.domain.entities.TicketType;
import com.steve.tickets.domain.entities.User;
import com.steve.tickets.repositories.EventRepository;
import com.steve.tickets.repositories.UserRepository;
import com.steve.tickets.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;


    @Override
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
return  eventRepository.findByOrganizerId(organizerId, pageable);
    }

    @Override
    public Optional<Event> getEventForOrganizer(UUID organizerId, UUID id) {
        return eventRepository.findByIdAndOrganizerId(id, organizerId);
    }
}
