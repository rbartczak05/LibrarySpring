package pl.lodz.p.library;

import io.mongock.runner.springboot.EnableMongock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongock
@SpringBootApplication(scanBasePackages = {
        "pl.lodz.p.library",
        "pl.lodz.p.library.adapters.rest",
        "pl.lodz.p.library.adapters.mongo",
        "pl.lodz.p.library.adapters.soap",
        "pl.lodz.p.library.services"
})
@EnableMongoRepositories(basePackages = "pl.lodz.p.library.adapters.mongo.repositories")
public class LibraryApplicationService {

    static void main(String[] args) {
        SpringApplication.run(LibraryApplicationService.class, args);
    }
}