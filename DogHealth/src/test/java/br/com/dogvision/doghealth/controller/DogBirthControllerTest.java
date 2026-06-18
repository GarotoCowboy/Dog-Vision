package br.com.dogvision.doghealth.controller;

import br.com.dogvision.doghealth.dto.create.CreateDogBirthRequest;
import br.com.dogvision.doghealth.dto.response.DogBirthResponse;
import br.com.dogvision.doghealth.infra.security.TokenService;
import br.com.dogvision.doghealth.service.DogsBirthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DogBirthController.class)
@AutoConfigureMockMvc(addFilters = false)
class DogBirthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private DogsBirthService service;

    @MockitoBean
    private TokenService tokenService;

    @Test
    void shouldCreateBirthRecord() throws Exception {
        UUID veterinarianId = UUID.randomUUID();
        CreateDogBirthRequest request = new CreateDogBirthRequest(
                UUID.randomUUID(),
                "Luna",
                "Border Collie",
                LocalDateTime.now(),
                4,
                "Healthy puppies",
                LocalDateTime.now().minusHours(1),
                LocalDateTime.now()
        );
        when(tokenService.getIdFromToken("token")).thenReturn(veterinarianId.toString());
        when(service.save(any(CreateDogBirthRequest.class), eq(veterinarianId))).thenReturn(response());

        mockMvc.perform(post("/api/v1/doghealth/birth")
                        .header("Authorization", "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numberOfPuppies").value(4));
    }

    @Test
    void shouldListBirthRecords() throws Exception {
        when(service.getAll()).thenReturn(List.of(response()));

        mockMvc.perform(get("/api/v1/doghealth/birth"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].dogsName").value("Luna"));
    }

    private DogBirthResponse response() {
        return new DogBirthResponse(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Luna",
                "Border Collie",
                LocalDateTime.now(),
                4,
                "Healthy puppies",
                LocalDateTime.now().minusHours(1),
                LocalDateTime.now()
        );
    }
}
