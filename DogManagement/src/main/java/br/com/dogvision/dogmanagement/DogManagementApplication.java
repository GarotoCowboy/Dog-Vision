package br.com.dogvision.dogmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DogManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(DogManagementApplication.class, args);
    }

}
