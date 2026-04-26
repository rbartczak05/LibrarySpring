package pl.lodz.p.library.integration;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import pl.lodz.p.library.adapters.rest.dto.ReaderDTO;
import pl.lodz.p.library.adapters.rest.dto.BookSetDTO;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class BookSetIntegrationTest extends BaseIntegrationTest {

    @Test
    void createBookSetTest() {
        BookSetDTO book = new BookSetDTO(null, "Clean Code", "Martin", 2008, 10);

        given()
                .contentType(ContentType.JSON)
                .body(book)
                .when()
                .post("/book_set")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("title", equalTo("Clean Code"));
    }

    @Test
    void getAllBookSetsTest() {
        createBook("T1", 5);
        createBook("T2", 5);

        given()
                .when()
                .get("/book_set")
                .then()
                .statusCode(200)
                .body("$", hasSize(greaterThanOrEqualTo(2)));
    }

    @Test
    void getBookSetByIdTest() {
        String id = createBook("Target", 1);
        given()
                .when()
                .get("/book_set/{id}", id)
                .then()
                .statusCode(200)
                .body("title", equalTo("Target"));
    }

    @Test
    void updateBookSetFailTest() {
        String id = createBook("Original", 5);
        BookSetDTO update = new BookSetDTO(null, "Updated", "Author", 2020, 100);

        given()
                .contentType(ContentType.JSON)
                .body(update)
                .when()
                .post("/book_set/{id}", id)
                .then()
                .statusCode(200)
                .body("title", equalTo("Original"))
                .body("quantity", equalTo(100));
    }

    @Test
    void deleteBookSetTest() {
        String id = createBook("To Delete", 0);

        given()
                .when()
                .delete("/book_set/{id}", id)
                .then()
                .statusCode(204);

        given().get("/book_set/{id}", id).then().statusCode(404);
    }

    @Test
    void createBookSetFailSyntaxTest() {
        BookSetDTO invalid = new BookSetDTO(null, "Title", "Author", 2020, -10);

        given()
                .contentType(ContentType.JSON)
                .body(invalid)
                .when()
                .post("/book_set")
                .then()
                .statusCode(400);
    }

    @Test
    void deleteBookSetFailWithActiveLoanTest() {
        String bookId = createBook("Loaned Book", 5);
        String userId = createUserAndActivate();

        given()
                .queryParam("readerId", userId)
                .queryParam("bookSetId", bookId)
                .post("/loans")
                .then().statusCode(201);

        given()
                .when()
                .delete("/book_set/{id}", bookId)
                .then()
                .statusCode(409);
    }

    private String createBook(String title, int qty) {
        BookSetDTO dto = new BookSetDTO(null, title, "Author", 2020, qty);
        return given()
                .contentType(ContentType.JSON)
                .body(dto)
                .post("/book_set")
                .then()
                .statusCode(201)
                .extract().path("id");
    }

    private String createUserAndActivate() {
        String shortId = UUID.randomUUID().toString().substring(0, 8);
        ReaderDTO reader = new ReaderDTO(null, "r" + shortId, "mail" + shortId + "@test.pl", 20, false, "reader", 0);
        String id = given()
                .contentType(ContentType.JSON)
                .body(reader)
                .post("/readers")
                .then().statusCode(201).extract().path("id");
        given().post("/readers/" + id + "/activate");
        return id;
    }
}