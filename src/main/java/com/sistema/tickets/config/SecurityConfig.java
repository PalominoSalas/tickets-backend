package com.sistema.tickets.config;

import com.sistema.tickets.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Permite las URLs del Frontend
        configuration.setAllowedOrigins(List.of("http://localhost:5173", "http://127.0.0.1:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept", "X-Requested-With"));
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // 1. PERMITIR PREFLIGHT (Peticiones OPTIONS del navegador)
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // 2. RUTAS PÚBLICAS Y SWAGGER
                .requestMatchers("/api/v1/auth/**", "/api/auth/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()

                // 3. REGLAS DE TICKETS (Soporta con y sin /v1/ y comodines)
                // Se usa hasAnyAuthority para evitar fallos si el rol no trae el prefijo "ROLE_"
                .requestMatchers(HttpMethod.POST, "/api/tickets/**", "/api/v1/tickets/**")
                    .hasAnyAuthority("CLIENTE", "ADMIN", "ROLE_CLIENTE", "ROLE_ADMIN")

                .requestMatchers(HttpMethod.GET, "/api/tickets/**", "/api/v1/tickets/**")
                    .hasAnyAuthority("CLIENTE", "AGENTE", "ADMIN", "SOPORTE", "ROLE_CLIENTE", "ROLE_AGENTE", "ROLE_ADMIN", "ROLE_SOPORTE")

                .requestMatchers(HttpMethod.PATCH, "/api/tickets/**", "/api/v1/tickets/**")
                    .hasAnyAuthority("CLIENTE", "AGENTE", "ADMIN", "SOPORTE", "ROLE_CLIENTE", "ROLE_AGENTE", "ROLE_ADMIN", "ROLE_SOPORTE")

                .requestMatchers(HttpMethod.PUT, "/api/tickets/**", "/api/v1/tickets/**")
                    .hasAnyAuthority("CLIENTE", "AGENTE", "ADMIN", "SOPORTE", "ROLE_CLIENTE", "ROLE_AGENTE", "ROLE_ADMIN", "ROLE_SOPORTE")

                // 4. CUALQUIER OTRA RUTA
                .anyRequest().authenticated()
            );

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}