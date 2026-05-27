package pl.lodz.p.library.migrations;

import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@ChangeUnit(id = "library-init-1", order = "001", author = "system")
public class InitialLibrarySetup {

    @Execution
    public void seedDatabase(MongoTemplate mongoTemplate) {
        long booksCount = mongoTemplate.getCollection("book_sets").countDocuments();

        UUID book1Id = UUID.fromString("33333333-3333-3333-3333-333333333333");
        UUID book2Id = UUID.fromString("44444444-4444-4444-4444-444444444444");
        UUID book3Id = UUID.fromString("55555555-5555-5555-5555-555555555555");
        UUID book4Id = UUID.fromString("66666666-6666-6666-6666-666666666666");
        UUID book5Id = UUID.fromString("77777777-7777-7777-7777-777777777777");

        if (booksCount == 0) {
            Document book1 = new Document("_id", book1Id)
                    .append("title", "Władca Pierścieni")
                    .append("author", "J.R.R. Tolkien")
                    .append("releaseYear", 1954)
                    .append("quantity", 5)
                    .append("_class", "pl.lodz.p.library.adapters.mongo.documents.BookSetDoc");

            Document book2 = new Document("_id", book2Id)
                    .append("title", "Wiedźmin: Ostatnie Życzenie")
                    .append("author", "Andrzej Sapkowski")
                    .append("releaseYear", 1993)
                    .append("quantity", 3)
                    .append("_class", "pl.lodz.p.library.adapters.mongo.documents.BookSetDoc");

            Document book3 = new Document("_id", book3Id)
                    .append("title", "Diuna")
                    .append("author", "Frank Herbert")
                    .append("releaseYear", 1965)
                    .append("quantity", 4)
                    .append("_class", "pl.lodz.p.library.adapters.mongo.documents.BookSetDoc");

            Document book4 = new Document("_id", book4Id)
                    .append("title", "Solaris")
                    .append("author", "Stanisław Lem")
                    .append("releaseYear", 1961)
                    .append("quantity", 2)
                    .append("_class", "pl.lodz.p.library.adapters.mongo.documents.BookSetDoc");

            Document book5 = new Document("_id", book5Id)
                    .append("title", "Fundacja")
                    .append("author", "Isaac Asimov")
                    .append("releaseYear", 1951)
                    .append("quantity", 6)
                    .append("_class", "pl.lodz.p.library.adapters.mongo.documents.BookSetDoc");

            mongoTemplate.getCollection("book_sets").insertMany(List.of(book1, book2, book3, book4, book5));
        }

        long clientsCount = mongoTemplate.getCollection("clients").countDocuments();

        UUID reader1Id = UUID.fromString("11111111-1111-1111-1111-111111111111");
        UUID reader2Id = UUID.fromString("22222222-2222-2222-2222-222222222222");

        if (clientsCount == 0) {
            Document client1 = new Document("_id", reader1Id)
                    .append("firstName", "Michał")
                    .append("lastName", "Wójcik")
                    .append("email", "reader1@library.com")
                    .append("age", 22)
                    .append("active", true)
                    .append("currentLoansCount", 1)
                    .append("_class", "pl.lodz.p.library.adapters.mongo.documents.ClientDoc");

            Document client2 = new Document("_id", reader2Id)
                    .append("firstName", "Agnieszka")
                    .append("lastName", "Kamińska")
                    .append("email", "reader2@library.com")
                    .append("age", 25)
                    .append("active", true)
                    .append("currentLoansCount", 1)
                    .append("_class", "pl.lodz.p.library.adapters.mongo.documents.ClientDoc");

            mongoTemplate.getCollection("clients").insertMany(List.of(client1, client2));
        }

        long loansCount = mongoTemplate.getCollection("loans").countDocuments();

        if (loansCount == 0) {
            Document loan1 = new Document("_id", UUID.randomUUID())
                    .append("active", true)
                    .append("startTime", LocalDateTime.now().minusDays(5))
                    .append("endTime", LocalDateTime.now().plusDays(25))
                    .append("returnTime", null)
                    .append("clientId", reader1Id)
                    .append("bookSetId", book1Id)
                    .append("_class", "pl.lodz.p.library.adapters.mongo.documents.LoanDoc");

            Document loan2 = new Document("_id", UUID.randomUUID())
                    .append("active", true)
                    .append("startTime", LocalDateTime.now().minusDays(10))
                    .append("endTime", LocalDateTime.now().plusDays(20))
                    .append("returnTime", null)
                    .append("clientId", reader2Id)
                    .append("bookSetId", book2Id)
                    .append("_class", "pl.lodz.p.library.adapters.mongo.documents.LoanDoc");

            mongoTemplate.getCollection("loans").insertMany(List.of(loan1, loan2));
        }
    }

    @RollbackExecution
    public void rollback(MongoTemplate mongoTemplate) {
        mongoTemplate.getCollection("book_sets").drop();
        mongoTemplate.getCollection("clients").drop();
        mongoTemplate.getCollection("loans").drop();
    }
}