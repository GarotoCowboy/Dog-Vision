package br.com.dogvision.dogfeeding;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DogFeedingApplication {

    public static void main(String[] args) {
        SpringApplication.run(DogFeedingApplication.class, args);
    }

}
