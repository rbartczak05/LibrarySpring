package pl.lodz.p.library.integration;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.lodz.p.library.ports.outbound.BookSetPort;
import pl.lodz.p.library.ports.outbound.ClientPort;
import pl.lodz.p.library.ports.outbound.LoanPort;

import java.util.Date;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public abstract class BaseIntegrationTest {

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

    @Value("${jwt.secret}")
    private String secretKey;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
        RestAssured.authentication = RestAssured.DEFAULT_AUTH;

        loanPort.deleteAll();
        clientPort.deleteAll();
        bookSetPort.deleteAll();

        String token = Jwts.builder()
                .subject("admin_test")
                .claim("role", "ROLE_ADMIN")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)))
                .compact();

        RestAssured.requestSpecification = new RequestSpecBuilder()
                .addHeader("Authorization", "Bearer " + token)
                .build();
    }

    @AfterEach
    void tearDown() {
        RestAssured.reset();
        loanPort.deleteAll();
        clientPort.deleteAll();
        bookSetPort.deleteAll();
    }
}