package br.com.dogvision.user.service.impl;

import br.com.dogvision.user.model.Role;
import br.com.dogvision.user.model.User;
import br.com.dogvision.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorizationServiceImpTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private AuthorizationServiceImp service;

    @Test
    void shouldLoadUserByUsername() {
        User user = new User();
        user.setRegistration("COL001");
        user.setPasswordHash("encoded");
        user.setRoles(Set.of(Role.ROLE_COLLABORATOR));

        when(repository.findByRegistration("COL001")).thenReturn(user);

        UserDetails result = service.loadUserByUsername("COL001");

        assertThat(result).isSameAs(user);
    }
}
