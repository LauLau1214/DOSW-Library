package edu.eci.dosw.DOSW_Library.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableJpaRepositories(
        basePackages = "edu.eci.dosw.DOSW_Library.persistence.relational.repository"
)
@EnableMongoRepositories(
        basePackages = "edu.eci.dosw.DOSW_Library.persistence.norelational.repository"
)
public class PersistenceConfig {
}