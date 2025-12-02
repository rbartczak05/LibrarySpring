package pl.lodz.p.library.integration;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import pl.lodz.p.library.dto.ReaderDTO;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ReaderRestTest extends BaseTestSetup {

    @Test
    void createReaderTest() {
        ReaderDTO reader = new ReaderDTO(null, "uniqueUser", "unique@mail.com", 25, false, "reader", 0);

        given()
                .contentType(ContentType.JSON)
                .body(reader)
                .when()
                .post("/readers")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("login", equalTo("uniqueUser"))
                .body("active", equalTo(false));
    }

    @Test
    void getAllReadersTest() {
        createReader("user1", "u1@mail.com");
        createReader("user2", "u2@mail.com");

        given()
                .when()
                .get("/readers")
                .then()
                .statusCode(200)
                .body("$", hasSize(greaterThanOrEqualTo(2)))
                .body("login", hasItems("user1", "user2"));
    }

    @Test
    void getReaderByIdTest() {
        String id = createReader("targetUser", "target@mail.com");

        given()
                .when()
                .get("/readers/{id}", id)
                .then()
                .statusCode(200)
                .body("id", equalTo(id))
                .body("login", equalTo("targetUser"));
    }

    @Test
    void updateReaderTest() {
        String id = createReader("oldData", "old@mail.com");
        ReaderDTO update = new ReaderDTO(null, "newData", "new@mail.com", 30, false, "reader", 0);

        given()
                .contentType(ContentType.JSON)
                .body(update)
                .when()
                .post("/readers/{id}", id)
                .then()
                .statusCode(200)
                .body("login", equalTo("newData"))
                .body("email", equalTo("new@mail.com"))
                .body("age", equalTo(30));
    }

    @Test
    void activateAndDeactivateReaderTest() {
        String id = createReader("statusUser", "status@mail.com");

        given()
                .post("/readers/{id}/activate", id)
                .then()
                .statusCode(200)
                .body("active", equalTo(true));

        given()
                .post("/readers/{id}/deactivate", id)
                .then()
                .statusCode(200)
                .body("active", equalTo(false));
    }

    @Test
    void createReaderFailSyntaxTest() {
        ReaderDTO invalid = new ReaderDTO(null, "", "mail@test.pl", -5, false, "reader", 0);

        given()
                .contentType(ContentType.JSON)
                .body(invalid)
                .when()
                .post("/readers")
                .then()
                .statusCode(409);
    }

    @Test
    void createReaderFailUniqueLoginTest() {
        createReader("duplicate", "mail1@test.pl");
        ReaderDTO duplicate = new ReaderDTO(null, "duplicate", "mail2@test.pl", 25, false, "reader", 0);

        given()
                .contentType(ContentType.JSON)
                .body(duplicate)
                .when()
                .post("/readers")
                .then()
                .statusCode(409);
    }

    private String createReader(String login, String email) {
        ReaderDTO reader = new ReaderDTO(null, login, email, 20, false, "reader", 0);
        return given()
                .contentType(ContentType.JSON)
                .body(reader)
                .post("/readers")
                .then()
                .statusCode(201)
                .extract().path("id");
    }
}