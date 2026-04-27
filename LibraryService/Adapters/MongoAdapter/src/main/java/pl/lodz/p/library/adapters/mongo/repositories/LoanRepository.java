package pl.lodz.p.library.adapters.mongo.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.lodz.p.library.adapters.mongo.documents.LoanDoc;

import java.util.List;

@Repository
public interface LoanRepository extends MongoRepository<LoanDoc, String> {
    List<LoanDoc> findByClientId(String clientId);

    List<LoanDoc> findByBookSetId(String bookSetId);

    List<LoanDoc> findByClientIdAndBookSetId(String clientId, String bookSetId);

    List<LoanDoc> findByActive(boolean active);

    List<LoanDoc> findByClientIdAndActive(String clientId, boolean active);

    List<LoanDoc> findByBookSetIdAndActive(String bookSetId, boolean active);
}