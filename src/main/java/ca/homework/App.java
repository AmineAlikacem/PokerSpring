package ca.homework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EntityScan("ca.homework.dto")
@EnableJpaRepositories("ca.homework.controller")
public class App {

    /**
     * Starts Spring boot
     * @param args args
     */
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
