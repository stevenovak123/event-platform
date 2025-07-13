package com.steve.tickets.repositories;

import com.steve.tickets.domain.entities.Event;
import com.steve.tickets.domain.entities.EventStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {
Page<Event> findByOrganizerId(UUID organizerId, Pageable pageable);
Optional<Event> findByIdAndOrganizerId(UUID id, UUID organizerId);
Page<Event> findByStatus(EventStatusEnum eventStatusEnum, Pageable pageable);
}
