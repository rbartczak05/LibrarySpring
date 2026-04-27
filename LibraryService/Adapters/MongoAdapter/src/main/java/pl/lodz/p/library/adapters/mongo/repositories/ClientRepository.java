package pl.lodz.p.library.adapters.mongo.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.lodz.p.library.adapters.mongo.documents.ClientDoc;

import java.util.List;

@Repository
public interface ClientRepository extends MongoRepository<ClientDoc, String> {
    List<ClientDoc> findByEmail(String email);
}