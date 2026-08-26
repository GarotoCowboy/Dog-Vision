package br.com.dogvision.doghealth.controller;

import br.com.dogvision.doghealth.dto.create.CreateMedicationRequest;
import br.com.dogvision.doghealth.dto.response.MedicationResponse;
import br.com.dogvision.doghealth.infra.security.TokenService;
import br.com.dogvision.doghealth.service.MedicationService;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MedicationController.class)
@AutoConfigureMockMvc(addFilters = false)
class MedicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MedicationService service;

    @MockitoBean
    private TokenService tokenService;

    @Test
    void shouldCreateMedication() throws Exception {
        CreateMedicationRequest request = new CreateMedicationRequest(
                "Thor",
                "Amoxicilina 250mg",
                LocalDate.of(2026, 6, 30)
        );
        when(service.save(any(CreateMedicationRequest.class))).thenReturn(response());

        mockMvc.perform(post("/api/v1/doghealth/medication")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.dogsName").value("Thor"))
                .andExpect(jsonPath("$.prescription").value("Amoxicilina 250mg"));
    }

    @Test
    void shouldListMedications() throws Exception {
        when(service.getAll()).thenReturn(List.of(response()));

        mockMvc.perform(get("/api/v1/doghealth/medication"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].dogsName").value("Thor"));
    }

    @Test
    void shouldGetMedicationById() throws Exception {
        UUID id = UUID.randomUUID();
        when(service.getById(id)).thenReturn(response());

        mockMvc.perform(get("/api/v1/doghealth/medication/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dogsName").value("Thor"));
    }

    @Test
    void shouldDeleteMedication() throws Exception {
        UUID id = UUID.randomUUID();
        doNothing().when(service).delete(id);

        mockMvc.perform(delete("/api/v1/doghealth/medication/{id}", id))
                .andExpect(status().isNoContent());
    }

    private MedicationResponse response() {
        return new MedicationResponse(
                UUID.randomUUID(),
                "Thor",
                "Amoxicilina 250mg",
                LocalDate.of(2026, 6, 30),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }
}

