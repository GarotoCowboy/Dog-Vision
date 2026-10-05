package br.com.dogvision.user.service.impl;

import br.com.dogvision.user.dto.events.UserCreatedEvent;
import br.com.dogvision.user.infra.exception.UserAlreadyExistsException;
import br.com.dogvision.user.infra.rabbit.RabbitConfig;
import br.com.dogvision.user.model.Role;
import br.com.dogvision.user.model.User;
import br.com.dogvision.user.repository.UserRepository;
import br.com.dogvision.user.service.UserService;
import br.com.dogvision.user.utils.RandomTemporaryPasswordGenerator;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RabbitTemplate rabbitTemplate;

    @Override
    @Transactional
    public User createAccount(String registration, String email, String name, Role role) {
        if (userRepository.existsByRegistration(registration)) {
            throw new UserAlreadyExistsException(registration);
        }

        String rawPassword = RandomTemporaryPasswordGenerator.generateRandomPassword(12);

        User user = new User();
        user.setRegistration(registration);
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        user.setRoles(Set.of(role));

        User savedUser = userRepository.save(user);

        UserCreatedEvent event = new UserCreatedEvent(
                name,
                email,
                registration,
                rawPassword
        );

        rabbitTemplate.convertAndSend(RabbitConfig.USER_EXCHANGE, RabbitConfig.USER_CREATED_ROUTING_KEY, event);

        return savedUser;
    }
}
