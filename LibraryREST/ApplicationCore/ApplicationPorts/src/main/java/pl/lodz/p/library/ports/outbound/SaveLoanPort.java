package pl.lodz.p.library.ports.outbound;

import pl.lodz.p.library.domain.model.Loan;

import java.time.LocalDateTime;
import java.util.Optional;

public interface SaveLoanPort {
    Optional<Loan> createLoan(String readerId, String bookSetId);

    Optional<Loan> createLoan(String readerId, String bookSetId, LocalDateTime loanStartTime);

    Optional<Loan> updateLoan(String loanId, Loan loanUpdates);

    Optional<Loan> endLoan(String loanId);
}
