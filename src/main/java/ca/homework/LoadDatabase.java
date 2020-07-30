package ca.homework;

import ca.homework.dto.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

@Configuration
public class LoadDatabase {
    /**
     * Logger
     */
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    /**
     * Configure the data source
     * @return the config of the data source
     */
    @Bean
    public DataSource dataSource() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        return builder.setType(EmbeddedDatabaseType.H2).build();
    }

    /**
     * Initialize the database
     * @param gameRepository Game repository
     * @return the specific action we want to preload
     */
    @Bean
    CommandLineRunner initDatabase(GameRepository gameRepository) {
        return args -> {
            log.info("Preloading " + gameRepository.save(new Game()));
        };
    }
}