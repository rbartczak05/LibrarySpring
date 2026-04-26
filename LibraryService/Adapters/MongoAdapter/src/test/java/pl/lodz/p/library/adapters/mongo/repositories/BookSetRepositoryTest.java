package pl.lodz.p.library.adapters.mongo.repositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.lodz.p.library.adapters.mongo.documents.BookSetDoc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest
@Testcontainers
class BookSetRepositoryTest {

    @Container
    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    @Autowired
    private BookSetRepository bookSetRepository;

    @BeforeEach
    void setUp() {
        bookSetRepository.save(new BookSetDoc("Test Title", "Test Author", 2020, 5));
        bookSetRepository.save(new BookSetDoc("Another Title", "Another Author", 2021, 0));
    }

    @AfterEach
    void tearDown() {
        bookSetRepository.deleteAll();
    }

    @Test
    void findBookByTitle() {
        List<BookSetDoc> books = bookSetRepository.findBookByTitle("Test Title");
        assertEquals(1, books.size());
    }

    @Test
    void findBooksByAuthor() {
        List<BookSetDoc> books = bookSetRepository.findBooksByAuthor("Another Author");
        assertEquals(1, books.size());
    }

    @Test
    void findBooksByReleaseYear() {
        List<BookSetDoc> books = bookSetRepository.findBooksByReleaseYear(2020);
        assertEquals(1, books.size());
    }

    @Test
    void findBooksByQuantity() {
        List<BookSetDoc> books = bookSetRepository.findBooksByQuantity(0);
        assertEquals(1, books.size());
    }

    @Test
    void findByQuantityGreaterThan() {
        List<BookSetDoc> books = bookSetRepository.findByQuantityGreaterThan(1);
        assertEquals(1, books.size());
    }
}