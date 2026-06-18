package br.com.dogvision.user.controller;

import br.com.dogvision.user.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class EmployeeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    void shouldCreateAndListEmployee() throws Exception {
        mockMvc.perform(post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "employee.integration@dogvision.com",
                                  "name": "Employee Integration",
                                  "phone": "11999999991",
                                  "registration": "EMP-INT-001",
                                  "password": "password@123",
                                  "shift": "MORNING",
                                  "type": "COLLABORATOR"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.registration").value("EMP-INT-001"))
                .andExpect(jsonPath("$.type").value("COLLABORATOR"));

        assertThat(employeeRepository.findAllWithUser())
                .anySatisfy(employee -> assertThat(employee.getEmail()).isEqualTo("employee.integration@dogvision.com"));

        mockMvc.perform(get("/api/v1/employees"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("employee.integration@dogvision.com")));
    }
}
