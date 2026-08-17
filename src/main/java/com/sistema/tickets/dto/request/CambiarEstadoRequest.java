package com.sistema.tickets.dto.request;

import com.sistema.tickets.model.enums.EstadoTicket;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CambiarEstadoRequest(
        @NotNull(message = "El nuevo estado es obligatorio")
        EstadoTicket nuevoEstado,

        @Size(max = 1000, message = "El comentario no puede exceder los 1000 caracteres")
        String comentarioOpcional
) {}