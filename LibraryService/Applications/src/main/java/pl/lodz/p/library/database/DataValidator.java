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
        database.getCollection("clients").drop();
        database.getCollection("booksets").drop();
        database.getCollection("loans").drop();

        Document clientValidator = new Document("$jsonSchema",
                new Document("bsonType", "object")
                        .append("required", Arrays.asList("firstName", "lastName", "email"))
                        .append("properties", new Document()
                                .append("firstName", new Document().append("bsonType", "string"))
                                .append("lastName", new Document().append("bsonType", "string"))
                                .append("email", new Document().append("bsonType", "string"))
                        )
        );

        createCollection(database, "clients", clientValidator);
        createCollection(database, "booksets", null);
        createCollection(database, "loans", null);
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