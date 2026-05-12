package br.com.dogvision.user.controller;

import br.com.dogvision.user.dto.create.CreateCoordinatorRequest;
import br.com.dogvision.user.dto.response.CoordinatorResponse;
import br.com.dogvision.user.infra.security.SecurityFilter;
import br.com.dogvision.user.model.EmployeeType;
import br.com.dogvision.user.model.ShiftEnum;
import br.com.dogvision.user.service.CoordinatorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CoordinatorController.class)
@AutoConfigureMockMvc(addFilters = false)
class CoordinatorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CoordinatorService service;

    @MockitoBean
    private SecurityFilter securityFilter;

    @Test
    void shouldGetCoordinatorById() throws Exception {
        UUID id = UUID.randomUUID();
        when(service.getById(id)).thenReturn(response());

        mockMvc.perform(get("/api/v1/employees/coordinators/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.registration").value("COORD001"));
    }

    @Test
    void shouldCreateCoordinator() throws Exception {
        CreateCoordinatorRequest request = new CreateCoordinatorRequest(
                "coordinator@dogvision.com",
                "Ana Silva",
                "11987654321",
                "COORD001",
                "password@123",
                ShiftEnum.MORNING
        );
        when(service.save(any(CreateCoordinatorRequest.class))).thenReturn(response());

        mockMvc.perform(post("/api/v1/employees/coordinators")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("coordinator@dogvision.com"));
    }

    private CoordinatorResponse response() {
        return new CoordinatorResponse(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "COORD001",
                "coordinator@dogvision.com",
                "Ana Silva",
                "11987654321",
                "MORNING",
                EmployeeType.COORDINATOR
        );
    }
}
