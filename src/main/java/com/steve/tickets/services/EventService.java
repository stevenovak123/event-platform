package com.steve.tickets.services;

import com.steve.tickets.domain.CreateEventRequest;
import com.steve.tickets.domain.UpdateEventRequest;
import com.steve.tickets.domain.entities.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface EventService {
    Event createEvent(UUID organiserID,CreateEventRequest eventRequest);
    Page<Event> listEventForOrganizer(UUID organizerId, Pageable pageable);
    Optional<Event> getEventForOrganizer(UUID organizerId, UUID id);
    Event updateEventForOrganizer(UUID organizerId, UUID id, UpdateEventRequest event);
    void deleteEventForOrganizer(UUID organizerId, UUID id);
}
