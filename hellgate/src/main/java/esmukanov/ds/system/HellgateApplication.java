package esmukanov.ds.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HellgateApplication {

    public static void main(String[] args) {
        SpringApplication.run(HellgateApplication.class, args);
    }
}
