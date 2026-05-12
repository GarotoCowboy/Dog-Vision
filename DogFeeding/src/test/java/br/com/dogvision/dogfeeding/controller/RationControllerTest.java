package br.com.dogvision.dogfeeding.controller;

import br.com.dogvision.dogfeeding.dto.create.CreateRationRequest;
import br.com.dogvision.dogfeeding.dto.response.RationAlertResponse;
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
                10.0,
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

    private RationResponse response() {
        return new RationResponse(
                UUID.randomUUID(),
                "Premium",
                RationType.NORMAL,
                10.0,
                5.0,
                LocalDate.now(),
                RationStockStatus.HEALTHY
        );
    }
}
