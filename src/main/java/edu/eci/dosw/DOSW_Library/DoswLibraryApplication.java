package edu.eci.dosw.DOSW_Library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class DoswLibraryApplication {

    public static void main(String[] args) {
        SpringApplication.run(DoswLibraryApplication.class, args);
    }
}