package pl.lodz.p.library.adapters.mongo.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.lodz.p.library.adapters.mongo.documents.ClientDoc;

import java.util.List;
import java.util.UUID;

@Repository
public interface ClientRepository extends MongoRepository<ClientDoc, UUID> {
    List<ClientDoc> findByEmail(String email);
}