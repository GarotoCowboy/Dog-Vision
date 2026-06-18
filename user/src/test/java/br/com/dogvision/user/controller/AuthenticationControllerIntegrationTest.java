package br.com.dogvision.user.controller;

import br.com.dogvision.user.infra.security.TokenService;
import br.com.dogvision.user.model.Role;
import br.com.dogvision.user.model.User;
import br.com.dogvision.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class AuthenticationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private TokenService tokenService;

    @Test
    void shouldAuthenticatePersistedUser() throws Exception {
        User user = new User();
        user.setRegistration("AUTH-001");
        user.setPasswordHash(passwordEncoder.encode("password@123"));
        user.setRoles(Set.of(Role.ROLE_COLLABORATOR));
        userRepository.saveAndFlush(user);

        when(tokenService.generateToken(any(User.class))).thenReturn("integration-token");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "registration": "AUTH-001",
                                  "password": "password@123"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("integration-token"));
    }
}
