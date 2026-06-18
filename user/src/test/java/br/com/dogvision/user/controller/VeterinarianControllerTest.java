package br.com.dogvision.user.controller;

import br.com.dogvision.user.dto.create.CreateVeterinarianRequest;
import br.com.dogvision.user.dto.response.VeterinarianResponse;
import br.com.dogvision.user.infra.security.SecurityFilter;
import br.com.dogvision.user.model.EmployeeType;
import br.com.dogvision.user.model.ShiftEnum;
import br.com.dogvision.user.service.VeterinarianService;
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

@WebMvcTest(controllers = VeterinarianController.class)
@AutoConfigureMockMvc(addFilters = false)
class VeterinarianControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private VeterinarianService service;

    @MockitoBean
    private SecurityFilter securityFilter;

    @Test
    void shouldGetVeterinarianById() throws Exception {
        UUID id = UUID.randomUUID();
        when(service.getById(id)).thenReturn(response());

        mockMvc.perform(get("/api/v1/employees/veterinarians/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.crmv").value("SP-12345"));
    }

    @Test
    void shouldCreateVeterinarian() throws Exception {
        CreateVeterinarianRequest request = new CreateVeterinarianRequest(
                "vet@dogvision.com",
                "Anna Costa",
                "11987654321",
                "VET001",
                "password@123",
                ShiftEnum.MORNING,
                "SP-12345",
                "General practice"
        );
        when(service.save(any(CreateVeterinarianRequest.class))).thenReturn(response());

        mockMvc.perform(post("/api/v1/employees/veterinarians")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.areaOfExpertise").value("General practice"));
    }

    private VeterinarianResponse response() {
        return new VeterinarianResponse(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "VET001",
                "vet@dogvision.com",
                "Anna Costa",
                "11987654321",
                "MORNING",
                EmployeeType.VETERINARIAN,
                "SP-12345",
                "General practice"
        );
    }
}
