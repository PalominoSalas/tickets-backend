package com.sistema.tickets.dto.response;

import java.time.LocalDateTime;

public record ComentarioResponse(
        Long id,
        String contenido,
        Boolean esInterno,
        UsuarioSummaryResponse autor,
        LocalDateTime fechaCreacion
) {}