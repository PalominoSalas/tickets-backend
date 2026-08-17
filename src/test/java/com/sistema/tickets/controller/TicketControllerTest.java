package com.sistema.tickets.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sistema.tickets.dto.request.AsignarAgenteRequest;
import com.sistema.tickets.dto.request.CambiarEstadoRequest;
import com.sistema.tickets.dto.request.CrearTicketRequest;
import com.sistema.tickets.dto.response.TicketResponse;
import com.sistema.tickets.dto.response.UsuarioSummaryResponse;
import com.sistema.tickets.model.enums.EstadoTicket;
import com.sistema.tickets.model.enums.PrioridadTicket;
import com.sistema.tickets.model.enums.Rol;
import com.sistema.tickets.service.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TicketController.class)
@AutoConfigureMockMvc(addFilters = false)
class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TicketService ticketService;

    private TicketResponse ticketResponse;
    private UsuarioSummaryResponse cliente;

    @BeforeEach
    void setUp() {
        cliente = new UsuarioSummaryResponse(1L, "Juan Pérez", "juan@example.com", Rol.CLIENTE);

        ticketResponse = TicketResponse.builder()
                .id(1L)
                .titulo("Error en pantalla de login")
                .descripcion("No permite ingresar con credenciales correctas")
                .estado(EstadoTicket.ABIERTO)
                .prioridad(PrioridadTicket.ALTA)
                .cliente(cliente)
                .comentarios(Collections.emptyList())
                .historialEstados(Collections.emptyList())
                .fechaCreacion(LocalDateTime.now())
                .fechaActualizacion(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("POST /api/tickets - Debe crear un ticket exitosamente")
    @WithMockUser(username = "juan@example.com", roles = "CLIENTE")
    void crearTicket_Exito() throws Exception {
        CrearTicketRequest request = new CrearTicketRequest("Error en pantalla de login", "No permite ingresar con credenciales correctas", PrioridadTicket.ALTA);
  
        when(ticketService.crearTicket(any(CrearTicketRequest.class), eq("juan@example.com")))
                .thenReturn(ticketResponse);

        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.titulo").value("Error en pantalla de login"))
                .andExpect(jsonPath("$.estado").value("ABIERTO"))
                .andExpect(jsonPath("$.prioridad").value("ALTA"));
    }

    @Test
    @DisplayName("GET /api/tickets/{id} - Debe obtener un ticket por ID")
    @WithMockUser(username = "juan@example.com", roles = "CLIENTE")
    void obtenerPorId_Exito() throws Exception {
        when(ticketService.obtenerPorId(eq(1L), eq("juan@example.com")))
                .thenReturn(ticketResponse);

        mockMvc.perform(get("/api/tickets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.titulo").value("Error en pantalla de login"));
    }

    @Test
    @DisplayName("GET /api/tickets - Debe filtrar tickets por parámetros")
    @WithMockUser(username = "admin@example.com", roles = "ADMIN")
    void filtrarTickets_Exito() throws Exception {
        when(ticketService.filtrarTickets(eq(EstadoTicket.ABIERTO), eq(PrioridadTicket.ALTA), eq(null), eq(null), eq("admin@example.com")))
                .thenReturn(List.of(ticketResponse));

        mockMvc.perform(get("/api/tickets")
                        .param("estado", "ABIERTO")
                        .param("prioridad", "ALTA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    @DisplayName("PATCH /api/tickets/{id}/estado - Debe cambiar el estado del ticket")
    @WithMockUser(username = "agente@example.com", roles = "AGENTE")
    void cambiarEstado_Exito() throws Exception {
        CambiarEstadoRequest request = new CambiarEstadoRequest(EstadoTicket.EN_PROCESO, "Se está investigando el problema");

        TicketResponse responseModificado = TicketResponse.builder()
                .id(1L)
                .titulo("Error en pantalla de login")
                .descripcion("No permite ingresar con credenciales correctas")
                .estado(EstadoTicket.EN_PROCESO)
                .prioridad(PrioridadTicket.ALTA)
                .cliente(cliente)
                .comentarios(Collections.emptyList())
                .historialEstados(Collections.emptyList())
                .fechaCreacion(LocalDateTime.now())
                .fechaActualizacion(LocalDateTime.now())
                .build();

        when(ticketService.cambiarEstado(eq(1L), any(CambiarEstadoRequest.class), eq("agente@example.com")))
                .thenReturn(responseModificado);

        mockMvc.perform(patch("/api/tickets/1/estado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("EN_PROCESO"));
    }

    @Test
    @DisplayName("PATCH /api/tickets/{id}/asignar - Debe asignar un agente al ticket")
    @WithMockUser(username = "admin@example.com", roles = "ADMIN")
    void asignarAgente_Exito() throws Exception {
        AsignarAgenteRequest request = new AsignarAgenteRequest(2L);

        UsuarioSummaryResponse agente = new UsuarioSummaryResponse(2L, "Carlos Agente", "agente@example.com", Rol.SOPORTE);

        TicketResponse responseAsignado = TicketResponse.builder()
                .id(1L)
                .titulo("Error en pantalla de login")
                .descripcion("No permite ingresar con credenciales correctas")
                .estado(EstadoTicket.ABIERTO)
                .prioridad(PrioridadTicket.ALTA)
                .cliente(cliente)
                .agenteAsignado(agente)
                .comentarios(Collections.emptyList())
                .historialEstados(Collections.emptyList())
                .fechaCreacion(LocalDateTime.now())
                .fechaActualizacion(LocalDateTime.now())
                .build();

        when(ticketService.asignarAgente(eq(1L), any(AsignarAgenteRequest.class), eq("admin@example.com")))
                .thenReturn(responseAsignado);

        mockMvc.perform(patch("/api/tickets/1/asignar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.agenteAsignado.id").value(2L))
                .andExpect(jsonPath("$.agenteAsignado.nombre").value("Carlos Agente"));
    }
}