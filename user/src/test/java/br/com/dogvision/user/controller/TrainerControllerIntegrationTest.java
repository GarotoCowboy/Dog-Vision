package br.com.dogvision.user.controller;

import br.com.dogvision.user.repository.TrainerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class TrainerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TrainerRepository trainerRepository;

    @MockitoBean
    private RabbitTemplate rabbitTemplate;

    @Test
    void shouldCreateAndFetchTrainerByRegistration() throws Exception {
        mockMvc.perform(post("/api/v1/employees/trainers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "trainer.integration@dogvision.com",
                                  "name": "Trainer Integration",
                                  "phone": "11999999994",
                                  "registration": "TRA-INT-001",
                                  "shift": "NIGHT",
                                  "areaOfExpertise": "Guide training"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.registration").value("TRA-INT-001"))
                .andExpect(jsonPath("$.areaOfExpertise").value("Guide training"));

        assertThat(trainerRepository.findAllWithUser()).hasSize(1);

        mockMvc.perform(get("/api/v1/employees/trainers/registration/TRA-INT-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Trainer Integration"));
    }
}
