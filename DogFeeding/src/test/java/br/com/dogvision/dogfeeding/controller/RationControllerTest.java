package br.com.dogvision.dogfeeding.controller;

import br.com.dogvision.dogfeeding.dto.create.CreateRationRequest;
import br.com.dogvision.dogfeeding.dto.response.RationAlertResponse;
import br.com.dogvision.dogfeeding.dto.response.RationConsumptionEstimateResponse;
import br.com.dogvision.dogfeeding.dto.response.RationResponse;
import br.com.dogvision.dogfeeding.infra.security.TokenService;
import br.com.dogvision.dogfeeding.model.RationStockStatus;
import br.com.dogvision.dogfeeding.model.RationType;
import br.com.dogvision.dogfeeding.service.RationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RationController.class)
@AutoConfigureMockMvc(addFilters = false)
class RationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RationService service;

    @MockitoBean
    private TokenService tokenService;

    @Test
    void shouldCreateRation() throws Exception {
        UUID loggedUserId = UUID.randomUUID();
        CreateRationRequest request = new CreateRationRequest(
                "Premium",
                RationType.NORMAL,
                5.0,
                LocalDate.now()
        );
        when(tokenService.getIdFromToken("token")).thenReturn(loggedUserId.toString());
        when(service.save(any(CreateRationRequest.class), eq(loggedUserId))).thenReturn(response());

        mockMvc.perform(post("/api/v1/dogfeeding/rations")
                        .header("Authorization", "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.stockStatus").value("HEALTHY"));
    }

    @Test
    void shouldSearchRations() throws Exception {
        when(service.search(RationType.NORMAL, RationStockStatus.HEALTHY)).thenReturn(List.of(response()));

        mockMvc.perform(get("/api/v1/dogfeeding/rations/search")
                        .param("rationType", "NORMAL")
                        .param("stockStatus", "HEALTHY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Premium"));
    }

    @Test
    void shouldReturnRationAlerts() throws Exception {
        RationAlertResponse alert = new RationAlertResponse(UUID.randomUUID(), "Premium", RationStockStatus.LOW);
        when(service.alerts()).thenReturn(List.of(alert));

        mockMvc.perform(get("/api/v1/dogfeeding/rations/alerts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].stockStatus").value("LOW"));
    }

    @Test
    void shouldReturnRationEstimate() throws Exception {
        UUID rationId = UUID.randomUUID();
        UUID dogId = UUID.randomUUID();
        RationConsumptionEstimateResponse estimate = new RationConsumptionEstimateResponse(
                rationId,
                "Premium",
                RationType.NORMAL,
                10.0,
                2.0,
                5.0,
                LocalDate.now().plusDays(5),
                RationStockStatus.HEALTHY,
                List.of(new br.com.dogvision.dogfeeding.dto.response.DogRationConsumptionResponse(
                        dogId,
                        UUID.randomUUID(),
                        "Plano Thor",
                        2.0
                ))
        );

        when(service.getEstimate(rationId)).thenReturn(estimate);

        mockMvc.perform(get("/api/v1/dogfeeding/rations/{id}/estimate", rationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rationName").value("Premium"))
                .andExpect(jsonPath("$.totalDailyConsumptionKg").value(2.0))
                .andExpect(jsonPath("$.estimatedDaysRemaining").value(5.0))
                .andExpect(jsonPath("$.dogConsumptions[0].dogId").value(dogId.toString()));
    }

    @Test
    void shouldReturnAllRationEstimates() throws Exception {
        UUID rationId = UUID.randomUUID();
        RationConsumptionEstimateResponse estimate = new RationConsumptionEstimateResponse(
                rationId,
                "Premium",
                RationType.NORMAL,
                10.0,
                2.0,
                5.0,
                LocalDate.now().plusDays(5),
                RationStockStatus.HEALTHY,
                List.of()
        );

        when(service.getAllEstimates()).thenReturn(List.of(estimate));

        mockMvc.perform(get("/api/v1/dogfeeding/rations/estimates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].rationName").value("Premium"))
                .andExpect(jsonPath("$[0].totalDailyConsumptionKg").value(2.0))
                .andExpect(jsonPath("$[0].estimatedDaysRemaining").value(5.0));
    }

    @Test
    void shouldIncreaseRationStock() throws Exception {
        UUID loggedUserId = UUID.randomUUID();
        UUID rationId = UUID.randomUUID();
        var request = new br.com.dogvision.dogfeeding.dto.update.IncreaseRationStockRequest(3, 15.0);

        when(tokenService.getIdFromToken("token")).thenReturn(loggedUserId.toString());
        when(service.increaseRation(eq(rationId), any(br.com.dogvision.dogfeeding.dto.update.IncreaseRationStockRequest.class), eq(loggedUserId)))
                .thenReturn(new RationResponse(rationId, "Premium", RationType.NORMAL, 50.0, LocalDate.now(), RationStockStatus.HEALTHY));

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch("/api/v1/dogfeeding/rations/{id}/increase", rationId)
                        .header("Authorization", "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentRationQuantity").value(50.0));
    }

    @Test
    void shouldDecreaseRationStock() throws Exception {
        UUID loggedUserId = UUID.randomUUID();
        UUID rationId = UUID.randomUUID();
        var request = new br.com.dogvision.dogfeeding.dto.update.DecreaseRationStockRequest(5.0);

        when(tokenService.getIdFromToken("token")).thenReturn(loggedUserId.toString());
        when(service.decreaseRation(eq(rationId), any(br.com.dogvision.dogfeeding.dto.update.DecreaseRationStockRequest.class), eq(loggedUserId)))
                .thenReturn(new RationResponse(rationId, "Premium", RationType.NORMAL, 10.0, LocalDate.now(), RationStockStatus.HEALTHY));

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch("/api/v1/dogfeeding/rations/{id}/decrease", rationId)
                        .header("Authorization", "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentRationQuantity").value(10.0));
    }

    private RationResponse response() {
        return new RationResponse(
                UUID.randomUUID(),
                "Premium",
                RationType.NORMAL,
                5.0,
                LocalDate.now(),
                RationStockStatus.HEALTHY
        );
    }
}
