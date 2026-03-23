package br.com.dogvision.user.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    @Autowired
    SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity){

        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/v1/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST,
                                "/api/v1/employees/trainers",
                                "/api/v1/employees/coordinators",
                                "/api/v1/employees/monitors",
                                "/api/v1/employees/veterinarians").hasAuthority("ROLE_COORDINATOR")
                        .requestMatchers(HttpMethod.DELETE,
                                "/api/v1/employees/trainers/*",
                                "/api/v1/employees/coordinators/*",
                                "/api/v1/employees/monitors/*",
                                "/api/v1/employees/veterinarians/*").hasAuthority("ROLE_COORDINATOR")
                        .requestMatchers(
                                "/api/v1/employees/veterinarians",
                                "/api/v1/employees/veterinarians/**").hasAnyAuthority("ROLE_VETERINARIAN","ROLE_COORDINATOR")
                        .requestMatchers(
                                "/api/v1/employees/trainers",
                                "/api/v1/employees/trainers/**").hasAnyAuthority("ROLE_TRAINER","ROLE_COORDINATOR")
                        .requestMatchers(
                                "/api/v1/employees/monitors",
                                "/api/v1/employees/monitors/**").hasAnyAuthority("ROLE_MONITOR","ROLE_COORDINATOR")
                        .requestMatchers(
                                "/api/v1/employees/coordinators",
                                "/api/v1/employees/coordinators/**").hasAuthority("ROLE_COORDINATOR")
                        .anyRequest().authenticated())
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
