package pl.lodz.p.library.adapters.mongo.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.lodz.p.library.adapters.mongo.documents.LoanDoc;

import java.util.List;
import java.util.UUID;

@Repository
public interface LoanRepository extends MongoRepository<LoanDoc, UUID> {
    List<LoanDoc> findByClientId(UUID clientId);

    List<LoanDoc> findByBookSetId(UUID bookSetId);

    List<LoanDoc> findByClientIdAndBookSetId(UUID clientId, UUID bookSetId);

    List<LoanDoc> findByActive(boolean active);

    List<LoanDoc> findByClientIdAndActive(UUID clientId, boolean active);

    List<LoanDoc> findByBookSetIdAndActive(UUID bookSetId, boolean active);
}