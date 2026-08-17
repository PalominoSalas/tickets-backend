package com.sistema.tickets.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AsignarAgenteRequest(
        @NotNull(message = "El ID del agente es obligatorio")
        @Positive(message = "El ID del agente debe ser un número positivo")
        Long agenteId
) {}