package br.com.dogvision.doghealth.controller;

import br.com.dogvision.doghealth.dto.create.CreateDogSurgeryRequest;
import br.com.dogvision.doghealth.dto.response.DogSurgeryResponse;
import br.com.dogvision.doghealth.infra.security.TokenService;
import br.com.dogvision.doghealth.model.EnumUrgency;
import br.com.dogvision.doghealth.service.DogSurgeryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SurgeryController.class)
@AutoConfigureMockMvc(addFilters = false)
class SurgeryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private DogSurgeryService service;

    @MockitoBean
    private TokenService tokenService;

    @Test
    void shouldCreateSurgery() throws Exception {
        UUID veterinarianId = UUID.randomUUID();
        CreateDogSurgeryRequest request = new CreateDogSurgeryRequest(
                UUID.randomUUID(),
                "Thor",
                "Golden",
                "Tumor removal",
                LocalDateTime.now().plusDays(3),
                "2 hours",
                EnumUrgency.NORMAL,
                true,
                "Fasting required"
        );

        when(tokenService.getIdFromToken("token")).thenReturn(veterinarianId.toString());
        when(service.save(any(CreateDogSurgeryRequest.class), eq(veterinarianId))).thenReturn(response());

        mockMvc.perform(post("/api/v1/doghealth/surgery")
                        .header("Authorization", "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Tumor removal"));
    }

    @Test
    void shouldListSurgeriesByDogId() throws Exception {
        UUID dogId = UUID.randomUUID();
        DogSurgeryResponse response = response(dogId);
        when(service.findAllByDogId(dogId)).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/doghealth/surgery/dog/{dogId}", dogId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].dogId").value(dogId.toString()));
    }

    @Test
    void shouldListSurgeriesByPeriod() throws Exception {
        LocalDateTime startsAt = LocalDateTime.of(2026, 5, 1, 0, 0);
        LocalDateTime endsAt = LocalDateTime.of(2026, 5, 31, 23, 59, 59);
        when(service.findByDateTimeOfSurgeryBetween(startsAt, endsAt, 0, 10))
                .thenReturn(new PageImpl<>(List.of(response())));

        mockMvc.perform(get("/api/v1/doghealth/surgery")
                        .param("startsAt", "2026-05-01T00:00:00")
                        .param("endsAt", "2026-05-31T23:59:59"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].urgency").value("NORMAL"));
    }

    @Test
    void shouldUpdateSurgery() throws Exception {
        UUID id = UUID.randomUUID();
        when(service.update(eq(id), any())).thenReturn(response());

        mockMvc.perform(patch("/api/v1/doghealth/surgery/update/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "durationExpected": "3 hours",
                                  "urgency": "HIGH",
                                  "onFasting": true,
                                  "observation": "Updated observation"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.durationExpected").value("2 hours"));
    }

    private DogSurgeryResponse response() {
        return response(UUID.randomUUID());
    }

    private DogSurgeryResponse response(UUID dogId) {
        return new DogSurgeryResponse(
                UUID.randomUUID(),
                UUID.randomUUID(),
                dogId,
                "Thor",
                "Golden",
                "Tumor removal",
                LocalDateTime.now().plusDays(3),
                "2 hours",
                EnumUrgency.NORMAL,
                true,
                "Fasting required",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }
}
