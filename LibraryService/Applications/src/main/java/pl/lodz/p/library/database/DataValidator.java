package pl.lodz.p.library.database;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.ValidationOptions;
import jakarta.annotation.PostConstruct;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

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

        Document clientValidator = new Document("$jsonSchema",
                new Document("bsonType", "object")
                        .append("required", Arrays.asList("_id", "firstName", "lastName", "email", "age", "active", "currentLoansCount"))
                        .append("properties", new Document()
                                .append("_id", new Document("bsonType", "binData"))
                                .append("firstName", new Document("bsonType", "string"))
                                .append("lastName", new Document("bsonType", "string"))
                                .append("email", new Document("bsonType", "string"))
                                .append("age", new Document("bsonType", "int"))
                                .append("active", new Document("bsonType", "bool"))
                                .append("currentLoansCount", new Document("bsonType", "int"))
                        )
        );

        Document bookSetValidator = new Document("$jsonSchema",
                new Document("bsonType", "object")
                        .append("required", Arrays.asList("_id", "title", "author", "releaseYear", "quantity"))
                        .append("properties", new Document()
                                .append("_id", new Document("bsonType", "binData"))
                                .append("title", new Document("bsonType", "string"))
                                .append("author", new Document("bsonType", "string"))
                                .append("releaseYear", new Document("bsonType", "int"))
                                .append("quantity", new Document("bsonType", "int"))
                        )
        );

        Document loanValidator = new Document("$jsonSchema",
                new Document("bsonType", "object")
                        .append("required", Arrays.asList("_id", "active", "startTime", "endTime", "clientId", "bookSetId"))
                        .append("properties", new Document()
                                .append("_id", new Document("bsonType", "binData"))
                                .append("active", new Document("bsonType", "bool"))
                                .append("startTime", new Document("bsonType", "date"))
                                .append("endTime", new Document("bsonType", "date"))
                                .append("returnTime", new Document("bsonType", Arrays.asList("date", "null")))
                                .append("clientId", new Document("bsonType", "binData"))
                                .append("bookSetId", new Document("bsonType", "binData"))
                        )
        );

        createOrUpdateCollection(database, "clients", clientValidator);
        createOrUpdateCollection(database, "booksets", bookSetValidator);
        createOrUpdateCollection(database, "loans", loanValidator);
    }

    private void createOrUpdateCollection(MongoDatabase database, String collectionName, Document validator) {
        boolean collectionExists = false;
        for (String name : database.listCollectionNames()) {
            if (name.equals(collectionName)) {
                collectionExists = true;
                break;
            }
        }

        if (!collectionExists) {
            CreateCollectionOptions options = new CreateCollectionOptions();
            if (validator != null) {
                options.validationOptions(new ValidationOptions().validator(validator));
            }
            database.createCollection(collectionName, options);
        } else if (validator != null) {
            database.runCommand(new Document("collMod", collectionName).append("validator", validator));
        }
    }
}