package br.com.dogvision.dogfeeding.controller;

import br.com.dogvision.dogfeeding.infra.security.TokenService;
import br.com.dogvision.dogfeeding.repository.RationRepository;
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
class RationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RationRepository rationRepository;

    @MockitoBean
    private TokenService tokenService;

    @Test
    void shouldCreateAndListRation() throws Exception {
        when(tokenService.getIdFromToken("test-token")).thenReturn(UUID.randomUUID().toString());

        mockMvc.perform(post("/api/v1/dogfeeding/rations")
                        .header("Authorization", "Bearer test-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Premium Integration",
                                  "rationType": "NORMAL",
                                  "totalRationQuantity": 20.0,
                                  "currentRationQuantity": 18.0,
                                  "registrationDate": "2026-05-12"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Premium Integration"))
                .andExpect(jsonPath("$.stockStatus").value("HEALTHY"));

        assertThat(rationRepository.findAll())
                .anySatisfy(ration -> assertThat(ration.getName()).isEqualTo("Premium Integration"));

        mockMvc.perform(get("/api/v1/dogfeeding/rations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].rationType").value("NORMAL"));
    }
}
