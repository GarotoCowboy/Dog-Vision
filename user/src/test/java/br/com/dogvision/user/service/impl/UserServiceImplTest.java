package br.com.dogvision.user.service.impl;

import br.com.dogvision.user.dto.events.UserCreatedEvent;
import br.com.dogvision.user.infra.exception.UserAlreadyExistsException;
import br.com.dogvision.user.infra.rabbit.RabbitConfig;
import br.com.dogvision.user.model.Role;
import br.com.dogvision.user.model.User;
import br.com.dogvision.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RabbitTemplate rabbitTemplate;

    private UserServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new UserServiceImpl(userRepository, passwordEncoder, rabbitTemplate);
    }

    @Test
    void shouldCreateAccountSuccessfully() {
        String registration = "FUNC001";
        String email = "maria@dogvision.com";
        String name = "Maria Oliveira";
        Role role = Role.ROLE_COLLABORATOR;

        when(userRepository.existsByRegistration(registration)).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setUserId(UUID.randomUUID());
            return u;
        });

        User result = service.createAccount(registration, email, name, role);

        assertThat(result).isNotNull();
        assertThat(result.getRegistration()).isEqualTo(registration);
        assertThat(result.getPasswordHash()).isEqualTo("encodedPassword");
        assertThat(result.getRoles()).contains(role);

        ArgumentCaptor<UserCreatedEvent> eventCaptor = ArgumentCaptor.forClass(UserCreatedEvent.class);
        verify(rabbitTemplate).convertAndSend(eq(RabbitConfig.USER_EXCHANGE), eq(RabbitConfig.USER_CREATED_ROUTING_KEY), eventCaptor.capture());

        UserCreatedEvent capturedEvent = eventCaptor.getValue();
        assertThat(capturedEvent.name()).isEqualTo(name);
        assertThat(capturedEvent.email()).isEqualTo(email);
        assertThat(capturedEvent.registration()).isEqualTo(registration);
        assertThat(capturedEvent.temporaryPassword()).isNotBlank();
    }

    @Test
    void shouldThrowExceptionWhenRegistrationAlreadyExists() {
        String registration = "FUNC001";
        when(userRepository.existsByRegistration(registration)).thenReturn(true);

        assertThatThrownBy(() -> service.createAccount(registration, "email@dogvision.com", "Name", Role.ROLE_COLLABORATOR))
                .isInstanceOf(UserAlreadyExistsException.class);

        verify(userRepository, never()).save(any());
        verify(rabbitTemplate, never()).convertAndSend(anyString(), anyString(), any(Object.class));
    }
}
