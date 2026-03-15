package pl.lodz.p.library.ports.infrastructure;

import pl.lodz.p.library.domain.model.Loan;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LoanRepositoryPort {
    Optional<Loan> findLoanById(String id);
    List<Loan> findLoansByReader(String readerId);
    List<Loan> findLoansByBookSet(String bookSetId);
    List<Loan> findLoansByReaderIdAndBookSetId(String readerId, String bookSetId);
    List<Loan> findAllLoans();
    Optional<Loan> createLoan(String readerId, String bookSetId);
    Optional<Loan> createLoan(String readerId, String bookSetId, LocalDateTime loanStartTime);
    Optional<Loan> updateLoan(String loanId, Loan loanUpdates);
    Optional<Loan> endLoan(String loanId);
    void deleteLoan(String loanId);
}
