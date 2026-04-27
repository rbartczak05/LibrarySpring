package pl.lodz.p.library.ports.inbound;

import pl.lodz.p.library.domain.model.Loan;

import java.time.LocalDateTime;
import java.util.List;

public interface LoanUseCase {
    Loan findLoanById(String id);

    List<Loan> findByClientId(String clientId);

    List<Loan> findByBookSetId(String bookSetId);

    List<Loan> findByClientIdAndBookSetId(String clientId, String bookSetId);

    List<Loan> findByActiveLoans(boolean active);

    List<Loan> findByClientIdAndActive(String clientId, boolean active);

    List<Loan> findByBookSetIdAndActive(String bookSetId, boolean active);

    List<Loan> findAllLoans();

    Loan createLoan(String clientId, String bookSetId);

    Loan createLoan(String clientId, String bookSetId, LocalDateTime loanStartTime);

    Loan updateLoan(String loanId, Loan loanUpdates);

    Loan endLoan(String loanId);

    void deleteLoan(String loanId);
}