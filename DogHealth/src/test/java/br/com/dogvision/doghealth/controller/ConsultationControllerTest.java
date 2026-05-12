package br.com.dogvision.doghealth.controller;

import br.com.dogvision.doghealth.dto.create.CreateConsultationRequest;
import br.com.dogvision.doghealth.dto.response.ConsultationResponse;
import br.com.dogvision.doghealth.dto.update.UpdateConsultationRequest;
import br.com.dogvision.doghealth.infra.security.TokenService;
import br.com.dogvision.doghealth.service.ConsultationService;
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
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ConsultationController.class)
@AutoConfigureMockMvc(addFilters = false)
class ConsultationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ConsultationService service;

    @MockitoBean
    private TokenService tokenService;

    @Test
    void shouldCreateConsultation() throws Exception {
        UUID veterinarianId = UUID.randomUUID();
        CreateConsultationRequest request = new CreateConsultationRequest(
                UUID.randomUUID(),
                "Antibiotic",
                "Thor",
                "Golden",
                "Dermatitis",
                LocalDate.now()
        );
        when(tokenService.getIdFromToken("token")).thenReturn(veterinarianId.toString());
        when(service.save(any(CreateConsultationRequest.class), eq(veterinarianId))).thenReturn(response());

        mockMvc.perform(post("/api/v1/doghealth/consultation")
                        .header("Authorization", "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.dogsName").value("Thor"));
    }

    @Test
    void shouldListConsultations() throws Exception {
        when(service.getAll()).thenReturn(List.of(response()));

        mockMvc.perform(get("/api/v1/doghealth/consultation"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].diagnosis").value("Dermatitis"));
    }

    @Test
    void shouldUpdateConsultation() throws Exception {
        UUID id = UUID.randomUUID();
        UpdateConsultationRequest request = new UpdateConsultationRequest("Rest", "Improved");
        when(service.update(eq(id), any(UpdateConsultationRequest.class))).thenReturn(response());

        mockMvc.perform(patch("/api/v1/doghealth/consultation/update/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.treatment").value("Antibiotic"));
    }

    private ConsultationResponse response() {
        return new ConsultationResponse(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Thor",
                "Golden",
                "Antibiotic",
                "Dermatitis",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }
}
