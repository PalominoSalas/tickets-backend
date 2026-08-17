package com.sistema.tickets.dto.request;

import com.sistema.tickets.model.enums.PrioridadTicket;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CrearTicketRequest(
        @NotBlank(message = "El título es obligatorio")
        @Size(min = 5, max = 150, message = "El título debe tener entre 5 y 150 caracteres")
        String titulo,

        @NotBlank(message = "La descripción es obligatoria")
        @Size(min = 10, max = 4000, message = "La descripción debe tener entre 10 y 4000 caracteres")
        String descripcion,

        @NotNull(message = "La prioridad es obligatoria")
        PrioridadTicket prioridad
) {}