package com.sistema.tickets.dto.response;

import com.sistema.tickets.model.enums.EstadoTicket;
import java.time.LocalDateTime;

public record HistorialEstadoResponse(
        Long id,
        EstadoTicket estadoAnterior,
        EstadoTicket estadoNuevo,
        UsuarioSummaryResponse modificadoPor,
        LocalDateTime fechaCambio
) {}