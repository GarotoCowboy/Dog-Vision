package br.com.dogvision.doghealth.controller;

import br.com.dogvision.doghealth.dto.create.CreateDogWeightRequest;
import br.com.dogvision.doghealth.dto.response.DogWeightResponse;
import br.com.dogvision.doghealth.infra.security.TokenService;
import br.com.dogvision.doghealth.service.DogWeightService;
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
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DogWeightController.class)
@AutoConfigureMockMvc(addFilters = false)
class DogWeightControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private DogWeightService service;

    @MockitoBean
    private TokenService tokenService;

    @Test
    void shouldCreateWeight() throws Exception {
        UUID collaboratorId = UUID.randomUUID();
        CreateDogWeightRequest request = new CreateDogWeightRequest(UUID.randomUUID(), 18.7);
        when(tokenService.getIdFromToken("token")).thenReturn(collaboratorId.toString());
        when(service.save(any(CreateDogWeightRequest.class), eq(collaboratorId))).thenReturn(response());

        mockMvc.perform(post("/api/v1/doghealth/weight")
                        .header("Authorization", "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.weight").value(18.7));
    }

    @Test
    void shouldListWeightByMonth() throws Exception {
        UUID dogId = UUID.randomUUID();
        DogWeightResponse response = response();
        when(service.getByMonth(dogId, 5, 2026)).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/doghealth/weight/{id}/month", dogId)
                        .param("month", "5")
                        .param("year", "2026"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].dogId").value(response.dogId().toString()));
    }

    @Test
    void shouldReturnLastWeight() throws Exception {
        UUID dogId = UUID.randomUUID();
        DogWeightResponse response = response();
        when(service.getLastWeight(dogId)).thenReturn(Optional.of(response));

        mockMvc.perform(get("/api/v1/doghealth/weight/{id}/lastWeight", dogId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dogId").value(response.dogId().toString()));
    }

    private DogWeightResponse response() {
        return new DogWeightResponse(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                18.7,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }
}
