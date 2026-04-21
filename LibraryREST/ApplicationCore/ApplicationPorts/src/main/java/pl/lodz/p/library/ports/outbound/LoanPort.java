package pl.lodz.p.library.ports.outbound;

import pl.lodz.p.library.domain.model.Loan;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LoanPort {
    Optional<Loan> findById(String id);

    List<Loan> findByReaderId(String readerId);

    List<Loan> findByBookSetId(String bookSetId);

    List<Loan> findByReaderIdAndBookSetId(String readerId, String bookSetId);

    List<Loan> findByActive(boolean active);

    List<Loan> findByBookSetIdAndActive(String bookSetId, boolean active);

    List<Loan> findByReaderIdAndActive(String readerId, boolean active);

    List<Loan> findAll();

    Optional<Loan> createLoan(String readerId, String bookSetId);

    Optional<Loan> createLoan(String readerId, String bookSetId, LocalDateTime loanStartTime);

    Optional<Loan> updateLoan(String loanId, Loan loanUpdates);

    Optional<Loan> endLoan(String loanId);

    void deleteLoan(String loanId);
}
