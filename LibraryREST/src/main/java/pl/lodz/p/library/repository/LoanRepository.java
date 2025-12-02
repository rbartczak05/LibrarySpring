package pl.lodz.p.library.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.lodz.p.library.model.Loan;

import java.util.List;

@Repository
public interface LoanRepository extends MongoRepository<Loan, String> {
    List<Loan> findByReaderId(String readerId);

    List<Loan> findByBookSetId(String bookSetId);

    List<Loan> findByReaderIdAndBookSetId(String readerId, String bookSetId);

    List<Loan> findByActive(boolean active);

    List<Loan> findByReaderIdAndActive(String readerId, boolean active);

    List<Loan> findByBookSetIdAndActive(String bookSetId, boolean active);
}
