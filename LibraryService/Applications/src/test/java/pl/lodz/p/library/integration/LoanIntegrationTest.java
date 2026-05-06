package pl.lodz.p.library.integration;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import pl.lodz.p.library.adapters.rest.dto.BookSetDTO;
import pl.lodz.p.library.domain.model.Client;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;

public class LoanIntegrationTest extends BaseIntegrationTest {

    @Test
    void createLoanTest() {
        UUID bookId = createBookSet(5);
        UUID clientId = createClient(true);

        given()
                .queryParam("clientId", clientId.toString())
                .queryParam("bookSetId", bookId.toString())
                .post("/loans")
                .then()
                .statusCode(201)
                .body("active", equalTo(true))
                .body("clientId", equalTo(clientId.toString()))
                .body("bookSetId", equalTo(bookId.toString()));
    }

    @Test
    void createLoanFutureDateTest() {
        UUID bookId = createBookSet(5);
        UUID clientId = createClient(true);
        String futureDate = LocalDateTime.now().plusDays(10).format(DateTimeFormatter.ISO_DATE_TIME);

        given()
                .queryParam("clientId", clientId.toString())
                .queryParam("bookSetId", bookId.toString())
                .queryParam("loanStartTime", futureDate)
                .post("/loans")
                .then()
                .statusCode(201)
                .body("startTime", startsWith(futureDate.substring(0, 16)));
    }

    @Test
    void endLoanTest() {
        UUID bookId = createBookSet(5);
        UUID clientId = createClient(true);

        String loanIdStr = given()
                .queryParam("clientId", clientId.toString())
                .queryParam("bookSetId", bookId.toString())
                .post("/loans")
                .then().statusCode(201)
                .extract().path("id");

        given()
                .post("/loans/{id}/end", loanIdStr)
                .then()
                .statusCode(200)
                .body("active", equalTo(false));
    }

    @Test
    void createLoanFailBookUnavailableTest() {
        UUID bookId = createBookSet(0);
        UUID clientId = createClient(true);

        given()
                .queryParam("clientId", clientId.toString())
                .queryParam("bookSetId", bookId.toString())
                .post("/loans")
                .then()
                .statusCode(409);
    }

    @Test
    void createLoanFailLimitExceededTest() {
        UUID bookId = createBookSet(10);
        UUID clientId = createClient(true);

        for (int i = 0; i < 5; i++) {
            given()
                    .queryParam("clientId", clientId.toString())
                    .queryParam("bookSetId", bookId.toString())
                    .post("/loans")
                    .then()
                    .statusCode(201);
        }

        given()
                .queryParam("clientId", clientId.toString())
                .queryParam("bookSetId", bookId.toString())
                .post("/loans")
                .then()
                .statusCode(409);
    }

    private UUID createClient(boolean active) {
        Client client = new Client("Anna", "Nowak", UUID.randomUUID().toString().substring(0, 8) + "@test.pl", 20);
        client.setActive(active);
        return clientPort.save(client).getId();
    }

    private UUID createBookSet(int quantity) {
        BookSetDTO dto = new BookSetDTO();
        dto.setTitle("Title");
        dto.setAuthor("Author");
        dto.setReleaseYear(2020);
        dto.setQuantity(quantity);
        String idStr = given().contentType(ContentType.JSON).body(dto).post("/book_set").then().statusCode(201).extract().path("id");
        return UUID.fromString(idStr);
    }
}