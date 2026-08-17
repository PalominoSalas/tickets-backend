package com.sistema.tickets.service.impl;

import com.sistema.tickets.dto.request.LoginRequest;
import com.sistema.tickets.dto.request.RegistroRequest;
import com.sistema.tickets.dto.response.AuthResponse;
import com.sistema.tickets.model.Usuario;
import com.sistema.tickets.model.enums.Rol;
import com.sistema.tickets.repository.UsuarioRepository;
import com.sistema.tickets.security.UserPrincipal;
import com.sistema.tickets.security.jwt.JwtUtils;
import com.sistema.tickets.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        String token = jwtUtils.generarToken(authentication);
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Usuario usuario = usuarioRepository.findByEmail(userPrincipal.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        return new AuthResponse(
                token,
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRol()
        );
    }

    @Override
    @Transactional
    public AuthResponse registrar(RegistroRequest request) {
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("El email ya se encuentra registrado.");
        }

        Rol rolAsignado = request.rol() != null ? request.rol() : Rol.CLIENTE;

        Usuario usuario = Usuario.builder()
                .nombre(request.nombre())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .rol(rolAsignado)
                .build();

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        // Autenticación automática tras el registro exitoso
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        String token = jwtUtils.generarToken(authentication);

        return new AuthResponse(
                token,
                usuarioGuardado.getId(),
                usuarioGuardado.getNombre(),
                usuarioGuardado.getEmail(),
                usuarioGuardado.getRol()
        );
    }
}