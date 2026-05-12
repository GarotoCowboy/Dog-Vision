package br.com.dogvision.user.controller;

import br.com.dogvision.user.repository.CoordinatorRepository;
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
class CoordinatorControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CoordinatorRepository coordinatorRepository;

    @Test
    void shouldCreateAndFetchCoordinatorByRegistration() throws Exception {
        mockMvc.perform(post("/api/v1/employees/coordinators")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "coordinator.integration@dogvision.com",
                                  "name": "Coordinator Integration",
                                  "phone": "11999999993",
                                  "registration": "COO-INT-001",
                                  "password": "password@123",
                                  "shift": "MORNING"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.registration").value("COO-INT-001"));

        assertThat(coordinatorRepository.findAll()).hasSize(1);

        mockMvc.perform(get("/api/v1/employees/coordinators/registration/COO-INT-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("coordinator.integration@dogvision.com"));
    }
}
