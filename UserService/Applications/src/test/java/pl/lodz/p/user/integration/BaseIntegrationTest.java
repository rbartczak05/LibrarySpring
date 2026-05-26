package pl.lodz.p.user.integration;

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
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.lodz.p.user.adapters.rest.security.JwtService;
import pl.lodz.p.user.domain.model.Administrator;
import pl.lodz.p.user.ports.inbound.UserUseCase;
import pl.lodz.p.user.ports.outbound.UserPort;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public abstract class BaseIntegrationTest {

    static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:8.3.2")
            .waitingFor(Wait.forListeningPort());
    static final RabbitMQContainer rabbitMQContainer = new RabbitMQContainer("rabbitmq:3-management");

    static String privateKeyBase64;
    static String publicKeyBase64;

    static {
        mongoDBContainer.start();
        rabbitMQContainer.start();

        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            KeyPair pair = keyGen.generateKeyPair();
            privateKeyBase64 = Base64.getEncoder().encodeToString(pair.getPrivate().getEncoded());
            publicKeyBase64 = Base64.getEncoder().encodeToString(pair.getPublic().getEncoded());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @LocalServerPort
    protected int port;

    @Autowired
    protected UserPort userPort;
    @Autowired
    protected UserUseCase userUseCase;
    @Autowired
    protected PasswordEncoder passwordEncoder;
    @Autowired
    protected JwtService jwtService;
    @Autowired
    protected UserDetailsService userDetailsService;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
        registry.add("spring.rabbitmq.host", rabbitMQContainer::getHost);
        registry.add("spring.rabbitmq.port", rabbitMQContainer::getAmqpPort);
        registry.add("jwt.private.key", () -> privateKeyBase64);
        registry.add("jwt.public.key", () -> publicKeyBase64);
        registry.add("jwt.secret", () -> "fallback-secret");
    }

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
        RestAssured.authentication = RestAssured.DEFAULT_AUTH;

        userPort.deleteAll();

        Administrator admin = new Administrator("admin_test", "admin@test.pl", "Admin", "Testowy", 30);
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