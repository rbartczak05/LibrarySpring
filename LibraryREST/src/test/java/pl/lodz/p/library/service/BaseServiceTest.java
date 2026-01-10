package pl.lodz.p.library.service;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.lodz.p.library.repository.BookSetRepository;
import pl.lodz.p.library.repository.LoanRepository;
import pl.lodz.p.library.repository.UserRepository;

@Testcontainers
@SpringBootTest
public abstract class BaseServiceTest {

    @Container
    static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    @Autowired
    protected BookSetRepository bookSetRepository;
    @Autowired
    protected LoanRepository loanRepository;
    @Autowired
    protected UserRepository userRepository;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getConnectionString);
    }

    @BeforeEach
    void cleanUp() {
        loanRepository.deleteAll();
        userRepository.deleteAll();
        bookSetRepository.deleteAll();
    }
}