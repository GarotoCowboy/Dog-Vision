package br.com.dogvision.doghealth.controller;

import br.com.dogvision.doghealth.infra.security.TokenService;
import br.com.dogvision.doghealth.repository.DogMassageRepository;
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
class DogMassageControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DogMassageRepository dogMassageRepository;

    @MockitoBean
    private TokenService tokenService;

    @Test
    void shouldCreateAndListDogMassageByDogId() throws Exception {
        UUID dogId = UUID.randomUUID();
        when(tokenService.getIdFromToken("test-token")).thenReturn(UUID.randomUUID().toString());

        mockMvc.perform(post("/api/v1/doghealth/massage")
                        .header("Authorization", "Bearer test-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "dogId": "%s",
                                  "date": "2026-05-12T14:15:00",
                                  "observations": "Relaxed after massage"
                                }
                                """.formatted(dogId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.dogId").value(dogId.toString()))
                .andExpect(jsonPath("$.observations").value("Relaxed after massage"));

        assertThat(dogMassageRepository.findAllByDogId(dogId)).hasSize(1);

        mockMvc.perform(get("/api/v1/doghealth/massage/" + dogId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].observations").value("Relaxed after massage"));
    }
}
