package home.sweethome.tripadvisor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "home.sweethome.tripadvisor.repository")
@EnableMongoRepositories(basePackages = "home.sweethome.tripadvisor.mongorepository")
public class TripAdvisor {

    public static void main(String[] args) {
        SpringApplication.run(TripAdvisor.class, args);
    }

}
