package br.com.dogvision.dogfeeding.controller;

import br.com.dogvision.dogfeeding.infra.security.TokenService;
import br.com.dogvision.dogfeeding.model.Ration;
import br.com.dogvision.dogfeeding.model.RationType;
import br.com.dogvision.dogfeeding.repository.FeedingRepository;
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
class FeedingControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RationRepository rationRepository;

    @Autowired
    private FeedingRepository feedingRepository;

    @MockitoBean
    private TokenService tokenService;

    @Test
    void shouldCreateFeedingAndReduceRationStock() throws Exception {
        UUID dogId = UUID.randomUUID();
        when(tokenService.getIdFromToken("test-token")).thenReturn(UUID.randomUUID().toString());

        Ration ration = new Ration();
        ration.setName("Feeding Ration");
        ration.setRationType(RationType.NORMAL);
        ration.setTotalRationQuantity(10);
        ration.setCurrentRationQuantity(8);
        ration.setRegistrationDate(LocalDate.of(2026, 5, 12));
        Ration savedRation = rationRepository.saveAndFlush(ration);

        mockMvc.perform(post("/api/v1/dogfeeding/feedings")
                        .header("Authorization", "Bearer test-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "dogId": "%s",
                                  "date": "2026-05-12",
                                  "feedingTime": "08:30:00",
                                  "mealType": "BREAKFAST",
                                  "items": [
                                    {
                                      "rationId": "%s",
                                      "quantityUsed": 1.5,
                                      "unit": "KILOGRAM"
                                    }
                                  ],
                                  "notes": "Ate everything",
                                  "dogResponse": "Calm"
                                }
                                """.formatted(dogId, savedRation.getId())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.dogId").value(dogId.toString()))
                .andExpect(jsonPath("$.totalQuantity").value(1.5));

        assertThat(feedingRepository.findAllByDogId(dogId)).hasSize(1);
        assertThat(rationRepository.findById(savedRation.getId())).get()
                .extracting(Ration::getCurrentRationQuantity)
                .isEqualTo(6.5);

        mockMvc.perform(get("/api/v1/dogfeeding/feedings/dog/" + dogId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].items[0].rationName").value("Feeding Ration"));
    }
}
