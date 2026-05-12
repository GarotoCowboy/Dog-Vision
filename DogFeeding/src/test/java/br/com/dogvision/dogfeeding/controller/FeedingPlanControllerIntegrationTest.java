package br.com.dogvision.dogfeeding.controller;

import br.com.dogvision.dogfeeding.infra.security.TokenService;
import br.com.dogvision.dogfeeding.model.Ration;
import br.com.dogvision.dogfeeding.model.RationType;
import br.com.dogvision.dogfeeding.repository.FeedingPlanRepository;
import br.com.dogvision.dogfeeding.repository.RationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
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
class FeedingPlanControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RationRepository rationRepository;

    @Autowired
    private FeedingPlanRepository feedingPlanRepository;

    @MockitoBean
    private TokenService tokenService;

    @Test
    void shouldCreateAndListFeedingPlanByDog() throws Exception {
        UUID dogId = UUID.randomUUID();
        when(tokenService.getIdFromToken("test-token")).thenReturn(UUID.randomUUID().toString());

        Ration ration = new Ration();
        ration.setName("Plan Ration");
        ration.setRationType(RationType.SPECIAL);
        ration.setTotalRationQuantity(30);
        ration.setCurrentRationQuantity(25);
        ration.setRegistrationDate(LocalDate.of(2026, 5, 12));
        Ration savedRation = rationRepository.saveAndFlush(ration);

        mockMvc.perform(post("/api/v1/dogfeeding/plans")
                        .header("Authorization", "Bearer test-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "dogId": "%s",
                                  "rationId": "%s",
                                  "name": "Plan Integration",
                                  "goal": "Weight maintenance",
                                  "dailyQuantity": 1.2,
                                  "unit": "KILOGRAM",
                                  "mealTypes": ["BREAKFAST", "DINNER"],
                                  "notes": "Split meals evenly",
                                  "startDate": "2026-05-12"
                                }
                                """.formatted(dogId, savedRation.getId())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.rationName").value("Plan Ration"))
                .andExpect(jsonPath("$.name").value("Plan Integration"));

        assertThat(feedingPlanRepository.findAllByDogId(dogId)).hasSize(1);

        mockMvc.perform(get("/api/v1/dogfeeding/plans/dog/" + dogId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].mealTypes[0]").value("BREAKFAST"));
    }
}
