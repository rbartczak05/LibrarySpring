package pl.lodz.p.library.ports.inbound;

import pl.lodz.p.library.domain.model.Loan;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface LoanUseCase {
    Loan findLoanById(UUID id);

    List<Loan> findByClientId(UUID clientId);

    List<Loan> findByBookSetId(UUID bookSetId);

    List<Loan> findByClientIdAndBookSetId(UUID clientId, UUID bookSetId);

    List<Loan> findByActiveLoans(boolean active);

    List<Loan> findByClientIdAndActive(UUID clientId, boolean active);

    List<Loan> findByBookSetIdAndActive(UUID bookSetId, boolean active);

    List<Loan> findAllLoans();

    Loan createLoan(UUID clientId, UUID bookSetId);

    Loan createLoan(UUID clientId, UUID bookSetId, LocalDateTime loanStartTime);

    Loan updateLoan(UUID loanId, Loan loanUpdates);

    Loan endLoan(UUID loanId);

    void deleteLoan(UUID loanId);
}