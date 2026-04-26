package pl.lodz.p.library.ports.inbound;

import pl.lodz.p.library.domain.model.Loan;

import java.time.LocalDateTime;
import java.util.List;

public interface LoanUseCase {
    Loan findLoanById(String id);

    List<Loan> findLoansByReader(String readerId);

    List<Loan> findLoansByBookSet(String bookSetId);

    List<Loan> findLoansByReaderIdAndBookSetId(String readerId, String bookSetId);

    List<Loan> findByActiveLoans(boolean active);

    List<Loan> findByReaderIdAndActive(String readerId, boolean active);

    List<Loan> findByBookSetIdAndActive(String bookSetId, boolean active);

    List<Loan> findAllLoans();

    Loan createLoan(String readerId, String bookSetId);

    Loan createLoan(String readerId, String bookSetId, LocalDateTime loanStartTime);

    Loan updateLoan(String loanId, Loan loanUpdates);

    Loan endLoan(String loanId);

    void deleteLoan(String loanId);
}