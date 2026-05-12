package br.com.dogvision.dogfeeding.controller;

import br.com.dogvision.dogfeeding.dto.create.CreateFeedingItemRequest;
import br.com.dogvision.dogfeeding.dto.create.CreateFeedingRequest;
import br.com.dogvision.dogfeeding.dto.response.ConsumptionReportResponse;
import br.com.dogvision.dogfeeding.dto.response.FeedingItemResponse;
import br.com.dogvision.dogfeeding.dto.response.FeedingResponse;
import br.com.dogvision.dogfeeding.infra.security.TokenService;
import br.com.dogvision.dogfeeding.model.MealType;
import br.com.dogvision.dogfeeding.model.MeasurementUnit;
import br.com.dogvision.dogfeeding.model.RationType;
import br.com.dogvision.dogfeeding.service.FeedingService;
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
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FeedingController.class)
@AutoConfigureMockMvc(addFilters = false)
class FeedingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private FeedingService service;

    @MockitoBean
    private TokenService tokenService;

    @Test
    void shouldCreateFeeding() throws Exception {
        UUID loggedUserId = UUID.randomUUID();
        CreateFeedingRequest request = new CreateFeedingRequest(
                UUID.randomUUID(),
                LocalDate.now(),
                LocalTime.of(8, 0),
                MealType.BREAKFAST,
                List.of(new CreateFeedingItemRequest(UUID.randomUUID(), 1.5, MeasurementUnit.KILOGRAM)),
                "normal feeding",
                "ate well"
        );
        when(tokenService.getIdFromToken("token")).thenReturn(loggedUserId.toString());
        when(service.save(any(CreateFeedingRequest.class), eq(loggedUserId))).thenReturn(response());

        mockMvc.perform(post("/api/v1/dogfeeding/feedings")
                        .header("Authorization", "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.mealType").value("BREAKFAST"));
    }

    @Test
    void shouldFindFeedingsByDogId() throws Exception {
        UUID dogId = UUID.randomUUID();
        when(service.findByDogId(dogId)).thenReturn(List.of(response()));

        mockMvc.perform(get("/api/v1/dogfeeding/feedings/dog/{dogId}", dogId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].notes").value("normal feeding"));
    }

    @Test
    void shouldReturnConsumptionReport() throws Exception {
        ConsumptionReportResponse report = new ConsumptionReportResponse(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Premium",
                2.5,
                MeasurementUnit.KILOGRAM,
                LocalDate.of(2026, 5, 1),
                LocalDate.of(2026, 5, 10)
        );
        when(service.consumptionReport(null, null, null, null)).thenReturn(List.of(report));

        mockMvc.perform(get("/api/v1/dogfeeding/feedings/reports/consumption"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].rationName").value("Premium"));
    }

    private FeedingResponse response() {
        return new FeedingResponse(
                UUID.randomUUID(),
                UUID.randomUUID(),
                LocalDate.now(),
                LocalTime.of(8, 0),
                MealType.BREAKFAST,
                List.of(new FeedingItemResponse(UUID.randomUUID(), "Premium", "NORMAL", 1.5, MeasurementUnit.KILOGRAM)),
                1.5,
                "normal feeding",
                "ate well",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }
}
