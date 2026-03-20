package br.com.dogvision.doghealth.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
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
public class SpringSecurityConfig {

    @Autowired
    SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET,"/api/v1/doghealth/consultation", "/api/v1/doghealth/consultation/**").authenticated() //all can get
                        .requestMatchers("/api/v1/doghealth/consultation", "/api/v1/doghealth/consultation/**").hasAuthority("ROLE_VETERINARIAN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/doghealth/weight/**","/api/v1/doghealth/weight").authenticated() //all can get
                        .requestMatchers("/api/v1/doghealth/weight", "/api/v1/doghealth/weight/**").hasAuthority("ROLE_MONITOR")
                        .requestMatchers(HttpMethod.GET,"/api/v1/doghealth/birth", "/api/v1/doghealth/birth/**").authenticated()
                        .requestMatchers("/api/v1/doghealth/birth", "/api/v1/doghealth/birth/**").hasAuthority("ROLE_VETERINARIAN") //all can get

                        //.requestMatchers("/api/v1/doghealth/**").hasAnyAuthority("ROLE_MONITOR", "ROLE_COORDINATOR","ROLE_VETERINARIAN","")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
