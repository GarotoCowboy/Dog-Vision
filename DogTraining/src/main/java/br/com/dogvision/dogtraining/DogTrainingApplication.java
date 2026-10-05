package br.com.dogvision.dogtraining;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DogTrainingApplication {

    public static void main(String[] args) {
        SpringApplication.run(DogTrainingApplication.class, args);
    }

}