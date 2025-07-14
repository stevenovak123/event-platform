package com.steve.tickets.controllers;

import com.steve.tickets.domain.dtos.GetPublishedEventDetailsResponseDto;
import com.steve.tickets.domain.dtos.ListPublishedEventsResponseDto;
import com.steve.tickets.domain.entities.Event;
import com.steve.tickets.mappers.EventMapper;
import com.steve.tickets.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/published-events")
@RequiredArgsConstructor
public class PublishedEventController {

    private final EventService eventService;
    private final EventMapper eventMapper;

    @GetMapping
    public ResponseEntity<Page<ListPublishedEventsResponseDto>> listPublishedEvents(@RequestParam(required = false) String q, Pageable pageable) {
        Page<Event> events;
        if (null != q && !q.trim().isEmpty()) {
            events = eventService.searchPublishedEvents(q, pageable);
        } else {
            events = eventService.listPublishedEvent(pageable);
        }

        return ResponseEntity.ok(events.map(eventMapper::toListPublishedEventsResponseDto));
    }

    @GetMapping(path = "/{eventId}")
    public ResponseEntity<GetPublishedEventDetailsResponseDto> getPublishedEventDetails(@PathVariable UUID eventId) {
        return eventService.getPublishedEvent(eventId).map(eventMapper::toGetPublishedEventDetailsResponseDto).map(ResponseEntity::ok
        ).orElse(ResponseEntity.notFound().build());
    }


}
