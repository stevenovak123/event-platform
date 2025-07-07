package com.steve.tickets.services;

import com.steve.tickets.domain.CreateEventRequest;
import com.steve.tickets.domain.entities.Event;

import java.util.UUID;

public interface EventService {
    Event createEvent(UUID organiserID,CreateEventRequest eventRequest);
}
