package com.steve.tickets.controllers;

import com.steve.tickets.domain.CreateEventRequest;
import com.steve.tickets.domain.UpdateEventRequest;
import com.steve.tickets.domain.dtos.*;
import com.steve.tickets.domain.entities.Event;
import com.steve.tickets.mappers.EventMapper;
import com.steve.tickets.services.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/events")
@RequiredArgsConstructor
public class EventController {
    private final EventMapper eventMapper;
    private final EventService eventService;

    @PostMapping
    public ResponseEntity<CreateEventResponseDto> createEvent(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody CreateEventRequestDto createEventRequestDto) {
        CreateEventRequest createEventRequest = eventMapper.fromDto(createEventRequestDto);
        UUID userId = parseUserId(jwt);

        Event createdEvent = eventService.createEvent(userId, createEventRequest);
        CreateEventResponseDto createEventResponseDto = eventMapper.toDto(createdEvent);

        return new ResponseEntity<>(createEventResponseDto, HttpStatus.CREATED);

    }

    @GetMapping
    public ResponseEntity<Page<ListEventResponseDto>> listEvents(@AuthenticationPrincipal Jwt jwt, Pageable pageable) {
        UUID userId = parseUserId(jwt);
        Page<Event> events = eventService.listEventForOrganizer(userId, pageable);
        return ResponseEntity.ok(events.map(eventMapper::toListEventResponseDto));

    }

    @GetMapping(path = "/{eventId}")
    public ResponseEntity<GetEventDetailsResponseDto> getEvent(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID eventId) {
        UUID userId = parseUserId(jwt);
        return eventService.getEventForOrganizer(userId, eventId).map(eventMapper::toGetEventDetailsResponseDto).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping(path = "/{eventId}")
    public ResponseEntity<UpdateEventResponseDto> updateEvent(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID eventId, @Valid @RequestBody UpdateEventRequestDto updateEventRequestDto) {
        UpdateEventRequest updateEventRequest = eventMapper.fromDto(updateEventRequestDto);
        UUID userId = parseUserId(jwt);

        Event updateEvent = eventService.updateEventForOrganizer(userId, eventId, updateEventRequest);
        UpdateEventResponseDto updateEventResponseDto = eventMapper.toUpdateEventResponseDto(updateEvent);

        return ResponseEntity.ok(updateEventResponseDto);

    }

    @DeleteMapping(path="/{eventId}")
    public ResponseEntity<Void> deleteEvent(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID eventId){
        UUID userId = parseUserId(jwt);
        eventService.deleteEventForOrganizer(userId, eventId);
        return ResponseEntity.noContent().build();
    }

    private UUID parseUserId(Jwt jwt) {
        return UUID.fromString(jwt.getSubject());
    }
}
