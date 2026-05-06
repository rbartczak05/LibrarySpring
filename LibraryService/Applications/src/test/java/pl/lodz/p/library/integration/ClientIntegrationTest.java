package pl.lodz.p.library.integration;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import pl.lodz.p.library.adapters.rest.dto.ClientDTO;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ClientIntegrationTest extends BaseIntegrationTest {

    @Test
    void createClientTest() {
        ClientDTO client = new ClientDTO();
        client.setFirstName("Jan");
        client.setLastName("Kowalski");
        client.setEmail("jan@mail.com");
        client.setAge(25);

        given()
                .contentType(ContentType.JSON)
                .body(client)
                .when()
                .post("/clients")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("firstName", equalTo("Jan"))
                .body("active", equalTo(false));
    }

    @Test
    void getAllClientsTest() {
        createClient("Anna", "a1@mail.com");
        createClient("Piotr", "p2@mail.com");

        given()
                .when()
                .get("/clients")
                .then()
                .statusCode(200)
                .body("_embedded.clients", hasSize(greaterThanOrEqualTo(2)))
                .body("_embedded.clients.firstName", hasItems("Anna", "Piotr"));
    }

    @Test
    void getClientByIdTest() {
        UUID id = createClient("Target", "target@mail.com");

        given()
                .when()
                .get("/clients/{id}", id.toString())
                .then()
                .statusCode(200)
                .body("id", equalTo(id.toString()))
                .body("firstName", equalTo("Target"));
    }

    @Test
    void createClientFailSyntaxTest() {
        ClientDTO invalid = new ClientDTO();
        invalid.setFirstName("");
        invalid.setLastName("Kowalski");
        invalid.setEmail("mail");
        invalid.setAge(-5);

        given()
                .contentType(ContentType.JSON)
                .body(invalid)
                .when()
                .post("/clients")
                .then()
                .statusCode(400);
    }

    private UUID createClient(String firstName, String email) {
        ClientDTO client = new ClientDTO();
        client.setFirstName(firstName);
        client.setLastName("Testowy");
        client.setEmail(email);
        client.setAge(20);

        String idStr = given()
                .contentType(ContentType.JSON)
                .body(client)
                .post("/clients")
                .then()
                .statusCode(201)
                .extract().path("id");
        return UUID.fromString(idStr);
    }
}