package br.com.dogvision.dogfeeding.controller;

import br.com.dogvision.dogfeeding.dto.create.CreateFeedingPlanRequest;
import br.com.dogvision.dogfeeding.dto.response.FeedingPlanResponse;
import br.com.dogvision.dogfeeding.infra.security.TokenService;
import br.com.dogvision.dogfeeding.model.MealType;
import br.com.dogvision.dogfeeding.model.MeasurementUnit;
import br.com.dogvision.dogfeeding.model.RationType;
import br.com.dogvision.dogfeeding.service.FeedingPlanService;
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
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FeedingPlanController.class)
@AutoConfigureMockMvc(addFilters = false)
class FeedingPlanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private FeedingPlanService service;

    @MockitoBean
    private TokenService tokenService;

    @Test
    void shouldCreateFeedingPlan() throws Exception {
        UUID loggedUserId = UUID.randomUUID();
        CreateFeedingPlanRequest request = new CreateFeedingPlanRequest(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Plano Senior",
                "Controle de peso",
                0.45,
                MeasurementUnit.KILOGRAM,
                List.of(MealType.BREAKFAST, MealType.DINNER),
                "Dividir em duas porcoes",
                LocalDate.now(),
                LocalDate.now().plusDays(30)
        );
        when(tokenService.getIdFromToken("token")).thenReturn(loggedUserId.toString());
        when(service.save(any(CreateFeedingPlanRequest.class), eq(loggedUserId))).thenReturn(response());

        mockMvc.perform(post("/api/v1/dogfeeding/plans")
                        .header("Authorization", "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Plano Senior"));
    }

    @Test
    void shouldFindPlansByDogId() throws Exception {
        UUID dogId = UUID.randomUUID();
        when(service.findByDogId(dogId)).thenReturn(List.of(response()));

        mockMvc.perform(get("/api/v1/dogfeeding/plans/dog/{dogId}", dogId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].rationName").value("Premium Senior"));
    }

    private FeedingPlanResponse response() {
        return new FeedingPlanResponse(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Premium Senior",
                RationType.SPECIAL,
                "Plano Senior",
                "Controle de peso",
                0.45,
                MeasurementUnit.KILOGRAM,
                List.of(MealType.BREAKFAST, MealType.DINNER),
                "Dividir em duas porcoes",
                LocalDate.now(),
                LocalDate.now().plusDays(30),
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }
}
