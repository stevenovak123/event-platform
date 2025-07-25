package com.steve.tickets.domain.dtos;
// used in the presentation layer.


import com.steve.tickets.domain.entities.EventStatusEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventRequestDto {
    @NotNull(message = "Event ID must be provided")
    private UUID id;

    @NotBlank(message="Event name is required")
    private String name;


    private LocalDateTime start;
    private LocalDateTime end;
    @NotBlank(message = "Venue Information is required")
    private String venue;

    private LocalDateTime salesStart;
    private LocalDateTime salesEnd;
    @NotNull(message = "Event Status must be provided")
    private EventStatusEnum status;

    @NotEmpty(message = "Atleast one TicketType is required.")
    @Valid
    private List<UpdateTicketTypeRequestDto> ticketTypes;
}
