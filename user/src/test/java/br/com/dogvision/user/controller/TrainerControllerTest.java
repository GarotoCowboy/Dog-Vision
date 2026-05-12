package br.com.dogvision.user.controller;

import br.com.dogvision.user.dto.create.CreateTrainerRequest;
import br.com.dogvision.user.dto.response.TrainerResponse;
import br.com.dogvision.user.infra.security.SecurityFilter;
import br.com.dogvision.user.model.EmployeeType;
import br.com.dogvision.user.model.ShiftEnum;
import br.com.dogvision.user.service.TrainerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TrainerController.class)
@AutoConfigureMockMvc(addFilters = false)
class TrainerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TrainerService service;

    @MockitoBean
    private SecurityFilter securityFilter;

    @Test
    void shouldListTrainers() throws Exception {
        when(service.getAll()).thenReturn(List.of(response()));

        mockMvc.perform(get("/api/v1/employees/trainers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].areaOfExpertise").value("Behavior"));
    }

    @Test
    void shouldCreateTrainer() throws Exception {
        CreateTrainerRequest request = new CreateTrainerRequest(
                "trainer@dogvision.com",
                "Pedro Almeida",
                "11987654321",
                "TRAIN001",
                "password@123",
                ShiftEnum.AFTERNOON,
                "Behavior"
        );
        when(service.save(any(CreateTrainerRequest.class))).thenReturn(response());

        mockMvc.perform(post("/api/v1/employees/trainers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.registration").value("TRAIN001"));
    }

    private TrainerResponse response() {
        return new TrainerResponse(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "TRAIN001",
                "trainer@dogvision.com",
                "Pedro Almeida",
                "11987654321",
                "AFTERNOON",
                EmployeeType.TRAINER,
                "Behavior"
        );
    }
}
