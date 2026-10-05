package br.com.dogvision.doghealth.controller;

import br.com.dogvision.doghealth.dto.create.CreateDogReproductionRequest;
import br.com.dogvision.doghealth.dto.response.DogReproductionResponse;
import br.com.dogvision.doghealth.infra.security.TokenService;
import br.com.dogvision.doghealth.service.DogReproductionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DogReproductionController.class)
@AutoConfigureMockMvc(addFilters = false)
class DogReproductionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private DogReproductionService service;

    @MockitoBean
    private TokenService tokenService;

    @Test
    void shouldCreateReproductionRecord() throws Exception {
        UUID veterinarianId = UUID.randomUUID();
        UUID dogId = UUID.randomUUID();
        CreateDogReproductionRequest request = new CreateDogReproductionRequest(
                dogId,
                "Mel",
                "Golden Retriever",
                LocalDate.of(2026, 4, 1),
                null,
                6,
                "Início do cio"
        );

        DogReproductionResponse response = response(dogId, veterinarianId);

        when(tokenService.getIdFromToken("token")).thenReturn(veterinarianId.toString());
        when(service.save(any(CreateDogReproductionRequest.class), eq(veterinarianId))).thenReturn(response);

        mockMvc.perform(post("/api/v1/doghealth/reproduction")
                        .header("Authorization", "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.dogsName").value("Mel"))
                .andExpect(jsonPath("$.cycleIntervalInMonths").value(6))
                .andExpect(jsonPath("$.expectedNextHeatDate").value("2026-10-01"));
    }

    @Test
    void shouldListReproductionRecords() throws Exception {
        UUID dogId = UUID.randomUUID();
        UUID vetId = UUID.randomUUID();
        when(service.getAll()).thenReturn(List.of(response(dogId, vetId)));

        mockMvc.perform(get("/api/v1/doghealth/reproduction"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].dogsName").value("Mel"));
    }

    @Test
    void shouldGetReproductionRecordById() throws Exception {
        UUID id = UUID.randomUUID();
        UUID dogId = UUID.randomUUID();
        UUID vetId = UUID.randomUUID();
        when(service.getById(id)).thenReturn(response(dogId, vetId));

        mockMvc.perform(get("/api/v1/doghealth/reproduction/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dogsName").value("Mel"));
    }

    @Test
    void shouldGetLastHeatByDogId() throws Exception {
        UUID dogId = UUID.randomUUID();
        UUID vetId = UUID.randomUUID();
        when(service.getLastHeatByDogId(dogId)).thenReturn(Optional.of(response(dogId, vetId)));

        mockMvc.perform(get("/api/v1/doghealth/reproduction/dog/{dogId}/last", dogId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dogsName").value("Mel"));
    }

    @Test
    void shouldDeleteReproductionRecord() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/api/v1/doghealth/reproduction/{id}", id))
                .andExpect(status().isNoContent());
    }

    private DogReproductionResponse response(UUID dogId, UUID veterinarianId) {
        return new DogReproductionResponse(
                UUID.randomUUID(),
                dogId,
                "Mel",
                "Golden Retriever",
                veterinarianId,
                LocalDate.of(2026, 4, 1),
                LocalDate.of(2026, 10, 1),
                6,
                "Início do cio",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }
}
