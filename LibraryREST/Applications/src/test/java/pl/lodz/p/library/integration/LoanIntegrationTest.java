package pl.lodz.p.library.integration;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import pl.lodz.p.library.adapters.rest.dto.BookSetDTO;
import pl.lodz.p.library.adapters.rest.dto.ReaderDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class LoanIntegrationTest extends BaseIntegrationTest {

    @Test
    void createLoanTest() {
        String bookId = createBookSet(5);
        String userId = createReader(true);

        given()
                .queryParam("readerId", userId)
                .queryParam("bookSetId", bookId)
                .post("/loans")
                .then()
                .statusCode(201)
                .body("active", equalTo(true))
                .body("readerId", equalTo(userId))
                .body("bookSetId", equalTo(bookId));
    }

    @Test
    void createLoanFutureDateTest() {
        String bookId = createBookSet(5);
        String userId = createReader(true);
        String futureDate = LocalDateTime.now().plusDays(10).format(DateTimeFormatter.ISO_DATE_TIME);

        given()
                .queryParam("readerId", userId)
                .queryParam("bookSetId", bookId)
                .queryParam("loanStartTime", futureDate)
                .post("/loans")
                .then()
                .statusCode(201)
                .body("startTime", startsWith(futureDate.substring(0, 19)));
    }

    @Test
    void getLoansByReaderTest() {
        String bookId = createBookSet(5);
        String userId = createReader(true);
        createLoan(userId, bookId);

        given()
                .when()
                .get("/loans/reader_id/{id}", userId)
                .then()
                .statusCode(200)
                .body("_embedded.loans", hasSize(1));
    }

    @Test
    void endLoanTest() {
        String bookId = createBookSet(5);
        String userId = createReader(true);
        String loanId = createLoan(userId, bookId);

        given()
                .when()
                .post("/loans/{id}/end", loanId)
                .then()
                .statusCode(200)
                .body("active", equalTo(false))
                .body("returnTime", notNullValue());
    }

    @Test
    void createLoanFailUserInactiveTest() {
        String bookId = createBookSet(5);
        String userId = createReader(false);

        given()
                .queryParam("readerId", userId)
                .queryParam("bookSetId", bookId)
                .post("/loans")
                .then()
                .statusCode(409);
    }

    @Test
    void createLoanFailResourceUnavailableTest() {
        String bookId = createBookSet(1);
        String user1 = createReader(true);
        String user2 = createReader(true);

        given()
                .queryParam("readerId", user1)
                .queryParam("bookSetId", bookId)
                .post("/loans")
                .then().statusCode(201);

        given()
                .queryParam("readerId", user2)
                .queryParam("bookSetId", bookId)
                .post("/loans")
                .then()
                .statusCode(409);
    }

    @Test
    void createLoanFailReaderMaxLoansExceededTest() {
        String bookId = createBookSet(10);

        String userId = createReader(true);

        for (int i = 0; i < 5; i++) {
            given()
                    .queryParam("readerId", userId)
                    .queryParam("bookSetId", bookId)
                    .post("/loans")
                    .then()
                    .statusCode(201);
        }

        given()
                .queryParam("readerId", userId)
                .queryParam("bookSetId", bookId)
                .post("/loans")
                .then()
                .statusCode(409);
    }

    private String createReader(boolean active) {
        String shortId = UUID.randomUUID().toString().substring(0, 8);
        ReaderDTO reader = new ReaderDTO(null, "u" + shortId, "user" + shortId + "@test.pl", 20, false, "reader", 0);
        String id = given().contentType(ContentType.JSON).body(reader).post("/readers").then().statusCode(201).extract().path("id");
        if (active) given().post("/readers/" + id + "/activate");
        return id;
    }

    private String createBookSet(int quantity) {
        BookSetDTO dto = new BookSetDTO(null, "Title", "Author", 2020, quantity);
        return given().contentType(ContentType.JSON).body(dto).post("/book_set").then().statusCode(201).extract().path("id");
    }

    private String createLoan(String userId, String bookId) {
        return given().queryParam("readerId", userId).queryParam("bookSetId", bookId).post("/loans").then().statusCode(201).extract().path("id");
    }
}