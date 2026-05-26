package pl.lodz.p.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(scanBasePackages = {
        "pl.lodz.p.user",
        "pl.lodz.p.user.adapters.rest",
        "pl.lodz.p.user.adapters.mongo",
        "pl.lodz.p.user.adapters.soap",
        "pl.lodz.p.user.adapters.rabbitmq",
        "pl.lodz.p.user.services"
})
@EnableMongoRepositories(basePackages = "pl.lodz.p.user.adapters.mongo.repositories")
public class LibraryApplicationUser {

    static void main(String[] args) {
        SpringApplication.run(LibraryApplicationUser.class, args);
    }
}