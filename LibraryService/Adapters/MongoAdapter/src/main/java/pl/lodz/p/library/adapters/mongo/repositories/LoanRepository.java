package pl.lodz.p.library.adapters.mongo.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.lodz.p.library.adapters.mongo.documents.LoanDoc;

import java.util.List;

@Repository
public interface LoanRepository extends MongoRepository<LoanDoc, String> {
    List<LoanDoc> findByReaderId(String readerId);

    List<LoanDoc> findByBookSetId(String bookSetId);

    List<LoanDoc> findByReaderIdAndBookSetId(String readerId, String bookSetId);

    List<LoanDoc> findByActive(boolean active);

    List<LoanDoc> findByReaderIdAndActive(String readerId, boolean active);

    List<LoanDoc> findByBookSetIdAndActive(String bookSetId, boolean active);
}
