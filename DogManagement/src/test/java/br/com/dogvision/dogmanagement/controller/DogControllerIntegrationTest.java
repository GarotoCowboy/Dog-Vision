package br.com.dogvision.dogmanagement.controller;

import br.com.dogvision.dogmanagement.repository.DogRepository;
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
class DogControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DogRepository dogRepository;

    @Test
    void shouldCreateAndListDog() throws Exception {
        mockMvc.perform(post("/api/v1/dogs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Luna Integration",
                                  "race": "LABRADOR",
                                  "status": "SOCIALIZACAO",
                                  "sex": "F",
                                  "dateOfBirth": "2024-01-10T08:30:00Z"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Luna Integration"));

        assertThat(dogRepository.findAll()).hasSize(1);

        mockMvc.perform(get("/api/v1/dogs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("SOCIALIZACAO"));
    }
}
