package pl.lodz.p.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(scanBasePackages = {
        "pl.lodz.p.library",
        "pl.lodz.p.library.adapters.rest",
        "pl.lodz.p.library.adapters.mongo",
        "pl.lodz.p.library.adapters.soap",
        "pl.lodz.p.library.services"
})
@EnableMongoRepositories(basePackages = "pl.lodz.p.library.adapters.mongo.repositories")
public class LibraryApplication {

    static void main(String[] args) {
        SpringApplication.run(LibraryApplication.class, args);
    }
}