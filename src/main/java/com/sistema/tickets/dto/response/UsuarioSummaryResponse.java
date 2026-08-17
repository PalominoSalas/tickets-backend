package com.sistema.tickets.dto.response;

import com.sistema.tickets.model.enums.Rol;

public record UsuarioSummaryResponse(
        Long id,
        String nombre,
        String email,
        Rol rol
) {}