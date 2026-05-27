package pl.lodz.p.user.migrations;

import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;
import java.util.UUID;

@ChangeUnit(id = "user-init-1", order = "001", author = "system")
public class InitialUserSetup {

    @Execution
    public void seedDatabase(MongoTemplate mongoTemplate) {
        long count = mongoTemplate.getCollection("users").countDocuments();
        if (count == 0) {
            Document admin1 = new Document("_id", UUID.randomUUID())
                    .append("login", "admin1")
                    .append("password", "$2a$12$gNM5hxyqzCqUHlr.cfAbQuCdhq5sQ6B7y3dgIAkm1aLIML6MRYi2K")
                    .append("email", "admin1@library.com")
                    .append("firstName", "Jan")
                    .append("lastName", "Kowalski")
                    .append("age", 35)
                    .append("active", true)
                    .append("_class", "pl.lodz.p.user.adapters.mongo.documents.AdministratorDoc");

            Document admin2 = new Document("_id", UUID.randomUUID())
                    .append("login", "admin2")
                    .append("password", "$2a$12$gNM5hxyqzCqUHlr.cfAbQuCdhq5sQ6B7y3dgIAkm1aLIML6MRYi2K")
                    .append("email", "admin2@library.com")
                    .append("firstName", "Anna")
                    .append("lastName", "Nowak")
                    .append("age", 42)
                    .append("active", true)
                    .append("_class", "pl.lodz.p.user.adapters.mongo.documents.AdministratorDoc");

            Document lib1 = new Document("_id", UUID.randomUUID())
                    .append("login", "lib1")
                    .append("password", "$2a$12$gNM5hxyqzCqUHlr.cfAbQuCdhq5sQ6B7y3dgIAkm1aLIML6MRYi2K")
                    .append("email", "lib1@library.com")
                    .append("firstName", "Piotr")
                    .append("lastName", "Zalewski")
                    .append("age", 28)
                    .append("active", true)
                    .append("_class", "pl.lodz.p.user.adapters.mongo.documents.LibrarianDoc");

            Document lib2 = new Document("_id", UUID.randomUUID())
                    .append("login", "lib2")
                    .append("password", "$2a$12$gNM5hxyqzCqUHlr.cfAbQuCdhq5sQ6B7y3dgIAkm1aLIML6MRYi2K")
                    .append("email", "lib2@library.com")
                    .append("firstName", "Katarzyna")
                    .append("lastName", "Wiśniewska")
                    .append("age", 31)
                    .append("active", true)
                    .append("_class", "pl.lodz.p.user.adapters.mongo.documents.LibrarianDoc");

            UUID reader1Id = UUID.fromString("11111111-1111-1111-1111-111111111111");
            Document reader1 = new Document("_id", reader1Id)
                    .append("login", "reader1")
                    .append("password", "$2a$12$gNM5hxyqzCqUHlr.cfAbQuCdhq5sQ6B7y3dgIAkm1aLIML6MRYi2K")
                    .append("email", "reader1@library.com")
                    .append("firstName", "Michał")
                    .append("lastName", "Wójcik")
                    .append("age", 22)
                    .append("active", true)
                    .append("_class", "pl.lodz.p.user.adapters.mongo.documents.ReaderDoc");

            UUID reader2Id = UUID.fromString("22222222-2222-2222-2222-222222222222");
            Document reader2 = new Document("_id", reader2Id)
                    .append("login", "reader2")
                    .append("password", "$2a$12$gNM5hxyqzCqUHlr.cfAbQuCdhq5sQ6B7y3dgIAkm1aLIML6MRYi2K")
                    .append("email", "reader2@library.com")
                    .append("firstName", "Agnieszka")
                    .append("lastName", "Kamińska")
                    .append("age", 25)
                    .append("active", true)
                    .append("_class", "pl.lodz.p.user.adapters.mongo.documents.ReaderDoc");

            mongoTemplate.getCollection("users").insertMany(List.of(admin1, admin2, lib1, lib2, reader1, reader2));
        }
    }

    @RollbackExecution
    public void rollback(MongoTemplate mongoTemplate) {
        mongoTemplate.getCollection("users").drop();
    }
}