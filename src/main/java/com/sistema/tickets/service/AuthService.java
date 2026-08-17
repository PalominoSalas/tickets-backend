package com.sistema.tickets.service;

import com.sistema.tickets.dto.request.LoginRequest;
import com.sistema.tickets.dto.request.RegistroRequest;
import com.sistema.tickets.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse login(LoginRequest request);
    AuthResponse registrar(RegistroRequest request);
}