package br.com.dogvision.user.controller;

import br.com.dogvision.user.repository.VeterinarianRepository;
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
class VeterinarianControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VeterinarianRepository veterinarianRepository;

    @Test
    void shouldCreateAndFetchVeterinarianByRegistration() throws Exception {
        mockMvc.perform(post("/api/v1/employees/veterinarians")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "vet.integration@dogvision.com",
                                  "name": "Vet Integration",
                                  "phone": "11999999995",
                                  "registration": "VET-INT-001",
                                  "password": "password@123",
                                  "shift": "MORNING",
                                  "crmv": "CRMV-INT-001",
                                  "areaOfExpertise": "Cardiology"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.registration").value("VET-INT-001"))
                .andExpect(jsonPath("$.crmv").value("CRMV-INT-001"));

        assertThat(veterinarianRepository.findAllWithUser()).hasSize(1);

        mockMvc.perform(get("/api/v1/employees/veterinarians/registration/VET-INT-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.areaOfExpertise").value("Cardiology"));
    }
}
