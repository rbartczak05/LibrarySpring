package pl.lodz.p.library.integration;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import pl.lodz.p.library.adapters.rest.dto.BookSetDTO;
import pl.lodz.p.library.domain.model.Client;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class BookSetIntegrationTest extends BaseIntegrationTest {

    @Test
    void createBookSetTest() {
        BookSetDTO book = new BookSetDTO();
        book.setTitle("Clean Code");
        book.setAuthor("Martin");
        book.setReleaseYear(2008);
        book.setQuantity(10);

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
                .statusCode(200);
    }

    @Test
    void getBookSetByIdTest() {
        String id = createBook("Target", 1);

        given()
                .when()
                .get("/book_set/{id}", id)
                .then()
                .statusCode(200)
                .body("id", equalTo(id))
                .body("title", equalTo("Target"));
    }

    @Test
    void deleteBookSetFailWithActiveLoanTest() {
        String bookId = createBook("Loaned Book", 5);
        String clientId = createActiveClient();

        given()
                .queryParam("clientId", clientId)
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
        BookSetDTO dto = new BookSetDTO();
        dto.setTitle(title);
        dto.setAuthor("Author");
        dto.setReleaseYear(2020);
        dto.setQuantity(qty);

        return given()
                .contentType(ContentType.JSON)
                .body(dto)
                .post("/book_set")
                .then()
                .statusCode(201)
                .extract().path("id");
    }

    private String createActiveClient() {
        Client client = new Client("John", "Doe", UUID.randomUUID().toString().substring(0, 8) + "@test.pl", 30);
        client.setActive(true);
        return clientPort.save(client).getId();
    }
}