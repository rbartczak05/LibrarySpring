package pl.lodz.p.library.integration;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.lodz.p.library.adapters.rest.security.JwtService;
import pl.lodz.p.library.domain.model.Administrator;
import pl.lodz.p.library.ports.inbound.UserUseCase;
import pl.lodz.p.library.ports.outbound.BookSetPort;
import pl.lodz.p.library.ports.outbound.LoanPort;
import pl.lodz.p.library.ports.outbound.UserPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public abstract class BaseIntegrationTest {

    static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    static {
        mongoDBContainer.start();
    }

    @LocalServerPort
    protected int port;

    @Autowired protected UserPort userPort;
    @Autowired protected BookSetPort bookSetPort;
    @Autowired protected LoanPort loanPort;

    @Autowired protected UserUseCase userUseCase;
    @Autowired protected PasswordEncoder passwordEncoder;

    @Autowired protected JwtService jwtService;
    @Autowired protected UserDetailsService userDetailsService;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
        RestAssured.authentication = RestAssured.DEFAULT_AUTH;

        loanPort.findAll().forEach(loan -> loanPort.deleteLoan(loan.getId()));
        userPort.findAllUsers().forEach(user -> userPort.deleteUser(user.getId()));
        bookSetPort.findAllBookSets().forEach(bookSet -> bookSetPort.deleteBookSet(bookSet.getId()));

        Administrator admin = new Administrator("admin_test", "admin@test.pl", 30);
        userUseCase.changeUserPasswordInModel(admin, passwordEncoder.encode("admin123"));
        userPort.addUser(admin);

        UserDetails userDetails = userDetailsService.loadUserByUsername("admin_test");
        String token = jwtService.generateAccessToken(userDetails);

        RestAssured.requestSpecification = new RequestSpecBuilder()
                .addHeader("Authorization", "Bearer " + token)
                .build();
    }

    @AfterEach
    void tearDown() {
        RestAssured.reset();
    }
}