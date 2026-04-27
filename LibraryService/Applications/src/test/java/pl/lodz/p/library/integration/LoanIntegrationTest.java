package pl.lodz.p.library.integration;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import pl.lodz.p.library.adapters.rest.dto.BookSetDTO;
import pl.lodz.p.library.domain.model.Client;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class LoanIntegrationTest extends BaseIntegrationTest {

    @Test
    void createLoanTest() {
        String bookId = createBookSet(5);
        String clientId = createClient(true);

        given()
                .queryParam("clientId", clientId)
                .queryParam("bookSetId", bookId)
                .post("/loans")
                .then()
                .statusCode(201)
                .body("active", equalTo(true))
                .body("clientId", equalTo(clientId))
                .body("bookSetId", equalTo(bookId));
    }

    @Test
    void createLoanFutureDateTest() {
        String bookId = createBookSet(5);
        String clientId = createClient(true);
        String futureDate = LocalDateTime.now().plusDays(10).format(DateTimeFormatter.ISO_DATE_TIME);

        given()
                .queryParam("clientId", clientId)
                .queryParam("bookSetId", bookId)
                .queryParam("loanStartTime", futureDate)
                .post("/loans")
                .then()
                .statusCode(201)
                .body("startTime", startsWith(futureDate.substring(0, 16)));
    }

    @Test
    void endLoanTest() {
        String bookId = createBookSet(5);
        String clientId = createClient(true);

        String loanId = given()
                .queryParam("clientId", clientId)
                .queryParam("bookSetId", bookId)
                .post("/loans")
                .then().statusCode(201)
                .extract().path("id");

        given()
                .post("/loans/{id}/end", loanId)
                .then()
                .statusCode(200)
                .body("active", equalTo(false));
    }

    @Test
    void createLoanFailBookUnavailableTest() {
        String bookId = createBookSet(0);
        String clientId = createClient(true);

        given()
                .queryParam("clientId", clientId)
                .queryParam("bookSetId", bookId)
                .post("/loans")
                .then()
                .statusCode(409);
    }

    @Test
    void createLoanFailLimitExceededTest() {
        String bookId = createBookSet(10);
        String clientId = createClient(true);

        for (int i = 0; i < 5; i++) {
            given()
                    .queryParam("clientId", clientId)
                    .queryParam("bookSetId", bookId)
                    .post("/loans")
                    .then()
                    .statusCode(201);
        }

        given()
                .queryParam("clientId", clientId)
                .queryParam("bookSetId", bookId)
                .post("/loans")
                .then()
                .statusCode(409);
    }

    private String createClient(boolean active) {
        Client client = new Client("Anna", "Nowak", UUID.randomUUID().toString().substring(0, 8) + "@test.pl", 20);
        client.setActive(active);
        return clientPort.save(client).getId();
    }

    private String createBookSet(int quantity) {
        BookSetDTO dto = new BookSetDTO();
        dto.setTitle("Title");
        dto.setAuthor("Author");
        dto.setReleaseYear(2020);
        dto.setQuantity(quantity);
        return given().contentType(ContentType.JSON).body(dto).post("/book_set").then().statusCode(201).extract().path("id");
    }
}