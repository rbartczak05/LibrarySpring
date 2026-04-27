package pl.lodz.p.user.database;

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

        Document userValidator = new Document("$jsonSchema",
                new Document("bsonType", "object")
                        .append("required", Arrays.asList("login", "email", "firstName", "lastName", "age"))
                        .append("properties", new Document()
                                .append("login", new Document().append("bsonType", "string").append("minLength", 3).append("maxLength", 20))
                                .append("email", new Document().append("bsonType", "string").append("minLength", 3).append("maxLength", 100).append("pattern", "^.+@.+$"))
                                .append("firstName", new Document().append("bsonType", "string").append("minLength", 1))
                                .append("lastName", new Document().append("bsonType", "string").append("minLength", 1))
                                .append("age", new Document().append("bsonType", "int").append("minimum", 1))
                                .append("active", new Document().append("bsonType", "bool"))
                        )
        );

        createCollection(database, "users", userValidator);
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