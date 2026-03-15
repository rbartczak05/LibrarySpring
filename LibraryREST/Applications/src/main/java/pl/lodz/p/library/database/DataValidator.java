package pl.lodz.p.library.database;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.ValidationOptions;
import jakarta.annotation.PostConstruct;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.stereotype.Component;
import pl.lodz.p.library.model.Reader;

import java.util.Arrays;

@Component
public class DataValidator {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public DataValidator(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @PostConstruct
    public void initDatabaseValidation() {
        MongoDatabase database = mongoTemplate.getDb();

        database.getCollection("users").drop();
        database.getCollection("booksets").drop();
        database.getCollection("loans").drop();

        Document userValidator = new Document("$jsonSchema",
                new Document("bsonType", "object")
                        .append("required", Arrays.asList("login", "email", "age"))
                        .append("properties", new Document()
                                .append("login", new Document()
                                        .append("bsonType", "string")
                                        .append("minLength", 3)
                                        .append("maxLength", 20)
                                        .append("description", "Login musi być ciągiem znaków od 3 do 20 znaków.")
                                )
                                .append("email", new Document()
                                        .append("bsonType", "string")
                                        .append("minLength", 3)
                                        .append("maxLength", 100)
                                        .append("pattern", "^.+@.+$")
                                        .append("description", "Email musi być poprawnym adresem email.")
                                )
                                .append("age", new Document()
                                        .append("bsonType", "int")
                                        .append("minimum", 1)
                                        .append("description", "Wiek musi być liczbą dodatnią.")
                                )
                                .append("currentLoansCount", new Document()
                                        .append("bsonType", "int")
                                        .append("minimum", 0)
                                        .append("maximum", Reader.maxLoans)
                                        .append("description", "Liczba wypożyczeń musi być między 0 a limitem.")
                                )
                                .append("active", new Document()
                                        .append("bsonType", "bool")
                                )
                        )
        );

        Document bookSetValidator = new Document("$jsonSchema",
                new Document("bsonType", "object")
                        .append("required", Arrays.asList("title", "author", "releaseYear", "quantity"))
                        .append("properties", new Document()
                                .append("title", new Document()
                                        .append("bsonType", "string")
                                        .append("minLength", 1)
                                        .append("description", "Tytuł nie może być pusty.")
                                )
                                .append("author", new Document()
                                        .append("bsonType", "string")
                                        .append("minLength", 1)
                                        .append("description", "Autor nie może być pusty.")
                                )
                                .append("releaseYear", new Document()
                                        .append("bsonType", "int")
                                        .append("minimum", 0)
                                        .append("description", "Rok wydania nie może być ujemny.")
                                )
                                .append("quantity", new Document()
                                        .append("bsonType", "int")
                                        .append("minimum", 0)
                                        .append("description", "Ilość nie może być ujemna.")
                                )
                        )
        );

        Document loanValidator = new Document("$jsonSchema",
                new Document("bsonType", "object")
                        .append("required", Arrays.asList("readerId", "bookSetId", "startTime", "active"))
                        .append("properties", new Document()
                                .append("readerId", new Document()
                                        .append("bsonType", "string")
                                        .append("minLength", 1)
                                        .append("description", "ID czytelnika jest wymagane.")
                                )
                                .append("bookSetId", new Document()
                                        .append("bsonType", "string")
                                        .append("minLength", 1)
                                        .append("description", "ID książki jest wymagane.")
                                )
                                .append("startTime", new Document()
                                        .append("bsonType", "date")
                                        .append("description", "Data rozpoczęcia jest wymagana.")
                                )
                                .append("active", new Document()
                                        .append("bsonType", "bool")
                                )
                                .append("returnTime", new Document()
                                        .append("bsonType", Arrays.asList("date", "null"))
                                )
                                .append("endTime", new Document()
                                        .append("bsonType", Arrays.asList("date", "null"))
                                )
                        )
        );

        createCollection(database, "users", userValidator);
        createCollection(database, "booksets", bookSetValidator);
        createCollection(database, "loans", loanValidator);

        mongoTemplate.indexOps("users").createIndex(new Index().on("login", Sort.Direction.ASC).unique());
        mongoTemplate.indexOps("users").createIndex(new Index().on("email", Sort.Direction.ASC).unique());
    }

    private void createCollection(MongoDatabase database, String collectionName, Document validator) {
        CreateCollectionOptions options = new CreateCollectionOptions();
        if (validator != null) {
            ValidationOptions validationOptions = new ValidationOptions().validator(validator);
            options.validationOptions(validationOptions);
        }
        database.createCollection(collectionName, options);
    }
}