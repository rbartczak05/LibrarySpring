package pl.lodz.p.library.ports.outbound;

import pl.lodz.p.library.domain.model.Loan;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LoanPort {
    Optional<Loan> findById(UUID id);

    List<Loan> findByClientId(UUID clientId);

    List<Loan> findByBookSetId(UUID bookSetId);

    List<Loan> findByClientIdAndBookSetId(UUID clientId, UUID bookSetId);

    List<Loan> findByActive(boolean active);

    List<Loan> findByClientIdAndActive(UUID clientId, boolean active);

    List<Loan> findByBookSetIdAndActive(UUID bookSetId, boolean active);

    List<Loan> findAll();

    Optional<Loan> createLoan(UUID clientId, UUID bookSetId);

    Optional<Loan> createLoan(UUID clientId, UUID bookSetId, LocalDateTime loanStartTime);

    Optional<Loan> updateLoan(UUID loanId, Loan loanUpdates);

    Optional<Loan> endLoan(UUID loanId);

    void deleteLoan(UUID loanId);

    void deleteAll();
}