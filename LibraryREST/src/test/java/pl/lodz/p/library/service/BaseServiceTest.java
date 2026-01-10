package pl.lodz.p.library.service;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import pl.lodz.p.library.repository.BookSetRepository;
import pl.lodz.p.library.repository.LoanRepository;
import pl.lodz.p.library.repository.UserRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseServiceTest {

    static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    static {
        mongoDBContainer.start();
    }

    @LocalServerPort
    protected int port;
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected BookSetRepository bookSetRepository;
    @Autowired
    protected LoanRepository loanRepository;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @BeforeEach
    void cleanUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";

        loanRepository.deleteAll();
        userRepository.deleteAll();
        bookSetRepository.deleteAll();
    }
}