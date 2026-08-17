package com.sistema.tickets.dto.response;

import com.sistema.tickets.model.enums.Rol;

public record AuthResponse(
        String token,
        String tipoToken,
        Long id,
        String nombre,
        String email,
        Rol rol
) {
    public AuthResponse(String token, Long id, String nombre, String email, Rol rol) {
        this(token, "Bearer", id, nombre, email, rol);
    }
}