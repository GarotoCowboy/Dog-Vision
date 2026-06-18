package br.com.dogvision.doghealth.controller;

import br.com.dogvision.doghealth.infra.security.TokenService;
import br.com.dogvision.doghealth.repository.ConsultationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class ConsultationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ConsultationRepository consultationRepository;

    @MockitoBean
    private TokenService tokenService;

    @Test
    void shouldCreateAndListConsultation() throws Exception {
        UUID dogId = UUID.randomUUID();
        UUID veterinarianId = UUID.randomUUID();
        when(tokenService.getIdFromToken("test-token")).thenReturn(veterinarianId.toString());

        mockMvc.perform(post("/api/v1/doghealth/consultation")
                        .header("Authorization", "Bearer test-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "dogId": "%s",
                                  "treatment": "Medication for 7 days",
                                  "dogsName": "Thor Integration",
                                  "dogsBreed": "Labrador",
                                  "diagnosis": "Dermatitis",
                                  "date": "2026-05-12"
                                }
                                """.formatted(dogId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.veterinarianId").value(veterinarianId.toString()))
                .andExpect(jsonPath("$.diagnosis").value("Dermatitis"));

        assertThat(consultationRepository.findAll()).hasSize(1);

        mockMvc.perform(get("/api/v1/doghealth/consultation"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].dogsName").value("Thor Integration"));
    }
}
