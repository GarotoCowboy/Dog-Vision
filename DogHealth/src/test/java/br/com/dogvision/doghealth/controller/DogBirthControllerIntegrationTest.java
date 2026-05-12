package br.com.dogvision.doghealth.controller;

import br.com.dogvision.doghealth.infra.security.TokenService;
import br.com.dogvision.doghealth.repository.DogBirthRepository;
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
class DogBirthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DogBirthRepository dogBirthRepository;

    @MockitoBean
    private TokenService tokenService;

    @Test
    void shouldCreateAndListDogBirth() throws Exception {
        UUID dogId = UUID.randomUUID();
        UUID veterinarianId = UUID.randomUUID();
        when(tokenService.getIdFromToken("test-token")).thenReturn(veterinarianId.toString());

        mockMvc.perform(post("/api/v1/doghealth/birth")
                        .header("Authorization", "Bearer test-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "dogId": "%s",
                                  "dogsName": "Luna Integration",
                                  "dogsBreed": "Golden Retriever",
                                  "date": "2026-05-12T10:30:00",
                                  "numberOfPuppies": 4,
                                  "observations": "Stable birth",
                                  "startTime": "2026-05-12T09:30:00",
                                  "endTime": "2026-05-12T11:00:00"
                                }
                                """.formatted(dogId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.veterinarianId").value(veterinarianId.toString()))
                .andExpect(jsonPath("$.numberOfPuppies").value(4));

        assertThat(dogBirthRepository.findAll()).hasSize(1);

        mockMvc.perform(get("/api/v1/doghealth/birth"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].dogsBreed").value("Golden Retriever"));
    }
}
