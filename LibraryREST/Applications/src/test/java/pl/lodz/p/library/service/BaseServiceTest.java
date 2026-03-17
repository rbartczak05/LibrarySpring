package pl.lodz.p.library.service;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.lodz.p.library.ports.outbound.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public abstract class BaseServiceTest {

    static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    static {
        mongoDBContainer.start();
    }

    @LocalServerPort
    protected int port;
    @Autowired protected GetUserPort getUserPort;
    @Autowired protected SaveUserPort saveUserPort;
    @Autowired protected DeleteUserPort deleteUserPort;

    @Autowired protected GetBookSetPort getBookSetPort;
    @Autowired protected SaveBookSetPort saveBookSetPort;
    @Autowired protected DeleteBookSetPort deleteBookSetPort;

    @Autowired protected GetLoanPort getLoanPort;
    @Autowired protected SaveLoanPort saveLoanPort;
    @Autowired protected DeleteLoanPort deleteLoanPort;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @BeforeEach
    void cleanUp() {
        getLoanPort.findAll().forEach(loan -> deleteLoanPort.deleteLoan(loan.getId()));
        getUserPort.findAllUsers().forEach(user -> deleteUserPort.deleteUser(user.getId()));
        getBookSetPort.findAllBookSets().forEach(bookSet -> deleteBookSetPort.deleteBookSet(bookSet.getId()));
    }
}