package br.com.dogvision.user.controller;

import br.com.dogvision.user.dto.create.CreateEmployeeRequest;
import br.com.dogvision.user.dto.response.EmployeeResponse;
import br.com.dogvision.user.dto.update.UpdateEmployeeRequest;
import br.com.dogvision.user.infra.security.SecurityFilter;
import br.com.dogvision.user.model.EmployeeType;
import br.com.dogvision.user.model.ShiftEnum;
import br.com.dogvision.user.service.EmployeeService;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EmployeeController.class)
@AutoConfigureMockMvc(addFilters = false)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EmployeeService service;

    @MockitoBean
    private SecurityFilter securityFilter;

    @Test
    void shouldListEmployees() throws Exception {
        when(service.getAll()).thenReturn(List.of(response()));

        mockMvc.perform(get("/api/v1/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].registration").value("EMP001"));
    }

    @Test
    void shouldCreateEmployee() throws Exception {
        CreateEmployeeRequest request = new CreateEmployeeRequest(
                "employee@dogvision.com",
                "Maria Oliveira",
                "11987654321",
                "EMP001",
                "password@123",
                ShiftEnum.NIGHT,
                EmployeeType.VETERINARIAN
        );
        when(service.save(any(CreateEmployeeRequest.class))).thenReturn(response());

        mockMvc.perform(post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.type").value("VETERINARIAN"));
    }

    @Test
    void shouldUpdateEmployee() throws Exception {
        UUID id = UUID.randomUUID();
        UpdateEmployeeRequest request = new UpdateEmployeeRequest("new@dogvision.com", "New Name", "11888887777", ShiftEnum.MORNING);
        when(service.update(eq(id), any(UpdateEmployeeRequest.class))).thenReturn(response());

        mockMvc.perform(patch("/api/v1/employees/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Maria Oliveira"));
    }

    private EmployeeResponse response() {
        return new EmployeeResponse(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "EMP001",
                "employee@dogvision.com",
                "Maria Oliveira",
                "11987654321",
                "NIGHT",
                EmployeeType.VETERINARIAN
        );
    }
}
