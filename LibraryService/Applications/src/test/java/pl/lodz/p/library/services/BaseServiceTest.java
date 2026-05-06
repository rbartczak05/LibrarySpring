package pl.lodz.p.library.services;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.lodz.p.library.ports.outbound.BookSetPort;
import pl.lodz.p.library.ports.outbound.ClientPort;
import pl.lodz.p.library.ports.outbound.LoanPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public abstract class BaseServiceTest {

    static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    static {
        mongoDBContainer.start();
    }

    @LocalServerPort
    protected int port;

    @Autowired
    protected ClientPort clientPort;
    @Autowired
    protected BookSetPort bookSetPort;
    @Autowired
    protected LoanPort loanPort;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @BeforeEach
    void cleanUp() {
        loanPort.deleteAll();
        clientPort.deleteAll();
        bookSetPort.deleteAll();
    }
}