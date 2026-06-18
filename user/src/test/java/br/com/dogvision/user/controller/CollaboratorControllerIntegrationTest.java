package br.com.dogvision.user.controller;

import br.com.dogvision.user.repository.CollaboratorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class CollaboratorControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CollaboratorRepository collaboratorRepository;

    @Test
    void shouldCreateAndFetchCollaboratorByRegistration() throws Exception {
        mockMvc.perform(post("/api/v1/employees/collaborators")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "collaborator.integration@dogvision.com",
                                  "name": "Collaborator Integration",
                                  "phone": "11999999992",
                                  "registration": "COL-INT-001",
                                  "password": "password@123",
                                  "shift": "AFTERNOON"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.registration").value("COL-INT-001"));

        assertThat(collaboratorRepository.findAllWithUser()).hasSize(1);

        mockMvc.perform(get("/api/v1/employees/collaborators/registration/COL-INT-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Collaborator Integration"));
    }
}
