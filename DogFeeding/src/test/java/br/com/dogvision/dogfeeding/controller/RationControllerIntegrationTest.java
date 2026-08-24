package br.com.dogvision.dogfeeding.controller;

import br.com.dogvision.dogfeeding.infra.security.TokenService;
import br.com.dogvision.dogfeeding.infra.rabbit.ration.RationQuantityConsumer;
import br.com.dogvision.dogfeeding.model.FeedingPlan;
import br.com.dogvision.dogfeeding.model.MeasurementUnit;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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

    @Autowired
    private FeedingPlanRepository feedingPlanRepository;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private RationQuantityConsumer rationQuantityConsumer;

    @MockitoBean
    private br.com.dogvision.dogfeeding.infra.rabbit.ration.RationEventPublisher rationEventPublisher;

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

    @Test
    void shouldIncreaseAndDecreaseRationStock() throws Exception {
        when(tokenService.getIdFromToken("test-token")).thenReturn(UUID.randomUUID().toString());

        Ration ration = new Ration();
        ration.setName("Stock Manage Ration");
        ration.setRationType(RationType.NORMAL);
        ration.setCurrentRationQuantity(10.0);
        ration.setRegistrationDate(LocalDate.now());
        ration = rationRepository.saveAndFlush(ration);

        // Increase by 2 bags of 15.0 kg (+30.0 kg -> 40.0 kg)
        mockMvc.perform(patch("/api/v1/dogfeeding/rations/{id}/increase", ration.getId())
                        .header("Authorization", "Bearer test-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "bagCount": 2,
                                  "weightPerBagKg": 15.0
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentRationQuantity").value(40.0));

        // Decrease by 5.0 kg (40.0 kg - 5.0 kg -> 35.0 kg)
        mockMvc.perform(patch("/api/v1/dogfeeding/rations/{id}/decrease", ration.getId())
                        .header("Authorization", "Bearer test-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "quantityKg": 5.0
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentRationQuantity").value(35.0));

        // Decrease by 50.0 kg (35.0 kg - 50.0 kg -> clamps to 0.0 kg)
        mockMvc.perform(patch("/api/v1/dogfeeding/rations/{id}/decrease", ration.getId())
                        .header("Authorization", "Bearer test-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "quantityKg": 50.0
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentRationQuantity").value(0.0));
    }

    @Test
    void shouldGetRationEstimateWithActivePlans() throws Exception {
        Ration ration = new Ration();
        ration.setName("Puppy Integration Plan");
        ration.setRationType(RationType.NORMAL);
        ration.setCurrentRationQuantity(12.0);
        ration.setRegistrationDate(LocalDate.now());
        ration = rationRepository.saveAndFlush(ration);

        UUID dogId = UUID.randomUUID();
        FeedingPlan plan = new FeedingPlan();
        plan.setDogId(dogId);
        plan.setRationId(ration.getId());
        plan.setName("Plano Dog");
        plan.setGoal("Crescimento");
        plan.setDailyQuantity(2.0);
        plan.setUnit(MeasurementUnit.KILOGRAM);
        plan.setStartDate(LocalDate.now().minusDays(1));
        plan.setActive(true);
        feedingPlanRepository.saveAndFlush(plan);

        mockMvc.perform(get("/api/v1/dogfeeding/rations/{id}/estimate", ration.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rationName").value("Puppy Integration Plan"))
                .andExpect(jsonPath("$.currentRationQuantityKg").value(12.0))
                .andExpect(jsonPath("$.totalDailyConsumptionKg").value(2.0))
                .andExpect(jsonPath("$.estimatedDaysRemaining").value(6.0))
                .andExpect(jsonPath("$.dogConsumptions[0].dogId").value(dogId.toString()))
                .andExpect(jsonPath("$.dogConsumptions[0].dailyQuantityKg").value(2.0));

        mockMvc.perform(get("/api/v1/dogfeeding/rations/estimates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
