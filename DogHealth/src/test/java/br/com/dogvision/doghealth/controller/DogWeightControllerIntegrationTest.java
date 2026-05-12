package br.com.dogvision.doghealth.controller;

import br.com.dogvision.doghealth.infra.security.TokenService;
import br.com.dogvision.doghealth.repository.DogWeightRepository;
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
class DogWeightControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DogWeightRepository dogWeightRepository;

    @MockitoBean
    private TokenService tokenService;

    @Test
    void shouldCreateAndListDogWeightByDogId() throws Exception {
        UUID dogId = UUID.randomUUID();
        UUID collaboratorId = UUID.randomUUID();
        when(tokenService.getIdFromToken("test-token")).thenReturn(collaboratorId.toString());

        mockMvc.perform(post("/api/v1/doghealth/weight")
                        .header("Authorization", "Bearer test-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "dogId": "%s",
                                  "weight": 18.7
                                }
                                """.formatted(dogId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.dogId").value(dogId.toString()))
                .andExpect(jsonPath("$.collaboratorId").value(collaboratorId.toString()))
                .andExpect(jsonPath("$.weight").value(18.7));

        assertThat(dogWeightRepository.findAllByDogId(dogId)).hasSize(1);

        mockMvc.perform(get("/api/v1/doghealth/weight/" + dogId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].weight").value(18.7));
    }
}
