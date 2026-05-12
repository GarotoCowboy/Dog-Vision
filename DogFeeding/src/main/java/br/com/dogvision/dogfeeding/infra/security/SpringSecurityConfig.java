package br.com.dogvision.dogfeeding.infra.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SpringSecurityConfig {

    private final SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/dogfeeding/rations/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/v1/dogfeeding/plans/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/v1/dogfeeding/rations").hasAnyAuthority("ROLE_COORDINATOR","ROLE_VETERINARIAN")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/dogfeeding/rations/**").hasAuthority("ROLE_COORDINATOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/dogfeeding/rations/**").hasAuthority("ROLE_COORDINATOR")
                        .requestMatchers(HttpMethod.POST, "/api/v1/dogfeeding/plans").hasAuthority("ROLE_VETERINARIAN")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/dogfeeding/plans/**").hasAuthority("ROLE_VETERINARIAN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/dogfeeding/plans/**").hasAuthority("ROLE_VETERINARIAN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/dogfeeding/feedings/**").authenticated()
                        .requestMatchers("/api/v1/dogfeeding/feedings/**").hasAnyAuthority("ROLE_COLLABORATOR", "ROLE_VETERINARIAN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
