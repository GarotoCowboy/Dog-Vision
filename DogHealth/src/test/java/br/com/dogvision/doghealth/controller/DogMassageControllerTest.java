package br.com.dogvision.doghealth.controller;

import br.com.dogvision.doghealth.dto.create.CreateDogMassageRequest;
import br.com.dogvision.doghealth.dto.response.DogMassageResponse;
import br.com.dogvision.doghealth.infra.security.TokenService;
import br.com.dogvision.doghealth.service.DogMassageService;
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

@WebMvcTest(controllers = DogMassageController.class)
@AutoConfigureMockMvc(addFilters = false)
class DogMassageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private DogMassageService service;

    @MockitoBean
    private TokenService tokenService;

    @Test
    void shouldCreateMassage() throws Exception {
        UUID collaboratorId = UUID.randomUUID();
        CreateDogMassageRequest request = new CreateDogMassageRequest(
                UUID.randomUUID(),
                LocalDateTime.now(),
                "Relaxed after massage"
        );
        when(tokenService.getIdFromToken("token")).thenReturn(collaboratorId.toString());
        when(service.save(any(CreateDogMassageRequest.class), eq(collaboratorId))).thenReturn(response());

        mockMvc.perform(post("/api/v1/doghealth/massage")
                        .header("Authorization", "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.observations").value("Relaxed after massage"));
    }

    @Test
    void shouldListMassageByWeek() throws Exception {
        UUID dogId = UUID.randomUUID();
        DogMassageResponse response = response();
        when(service.getByWeek(eq(dogId), any(LocalDate.class))).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/doghealth/massage/{id}/week", dogId)
                        .param("date", "2026-05-13"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].dogId").value(response.dogId().toString()));
    }

    @Test
    void shouldReturnLastMassage() throws Exception {
        UUID dogId = UUID.randomUUID();
        DogMassageResponse response = response();
        when(service.getLastMassage(dogId)).thenReturn(Optional.of(response));

        mockMvc.perform(get("/api/v1/doghealth/massage/{id}/lastWeight", dogId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dogId").value(response.dogId().toString()));
    }

    private DogMassageResponse response() {
        return new DogMassageResponse(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Relaxed after massage",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }
}
