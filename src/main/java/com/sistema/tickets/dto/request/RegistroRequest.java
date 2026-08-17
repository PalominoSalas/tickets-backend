package com.sistema.tickets.dto.request;

import com.sistema.tickets.model.enums.Rol;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistroRequest(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
        String nombre,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El formato de email no es válido")
        String email,

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 6, max = 40, message = "La contraseña debe tener entre 6 y 40 caracteres")
        String password,

        Rol rol // Opcional: si viene nulo, se asigna CLIENTE por defecto
) {}