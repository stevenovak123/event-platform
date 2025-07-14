package com.steve.tickets.repositories;

import com.steve.tickets.domain.entities.TicketValidation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface TicketValidationRepository extends JpaRepository<TicketValidation, UUID> {
}
