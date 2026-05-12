package br.com.dogvision.user.controller;

import br.com.dogvision.user.dto.create.CreateCollaboratorRequest;
import br.com.dogvision.user.dto.response.CollaboratorResponse;
import br.com.dogvision.user.infra.security.SecurityFilter;
import br.com.dogvision.user.model.EmployeeType;
import br.com.dogvision.user.model.ShiftEnum;
import br.com.dogvision.user.service.CollaboratorService;
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

@WebMvcTest(controllers = CollaboratorController.class)
@AutoConfigureMockMvc(addFilters = false)
class CollaboratorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CollaboratorService service;

    @MockitoBean
    private SecurityFilter securityFilter;

    @Test
    void shouldListCollaborators() throws Exception {
        when(service.getAll()).thenReturn(List.of(response()));

        mockMvc.perform(get("/api/v1/employees/collaborators"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].registration").value("COL001"));
    }

    @Test
    void shouldCreateCollaborator() throws Exception {
        CreateCollaboratorRequest request = new CreateCollaboratorRequest(
                "collaborator@dogvision.com",
                "Carlos Souza",
                "11987654321",
                "COL001",
                "password@123",
                ShiftEnum.MORNING
        );
        when(service.save(any(CreateCollaboratorRequest.class))).thenReturn(response());

        mockMvc.perform(post("/api/v1/employees/collaborators")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Carlos Souza"));
    }

    private CollaboratorResponse response() {
        return new CollaboratorResponse(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "COL001",
                "collaborator@dogvision.com",
                "Carlos Souza",
                "11987654321",
                EmployeeType.COLLABORATOR,
                "MORNING"
        );
    }
}
