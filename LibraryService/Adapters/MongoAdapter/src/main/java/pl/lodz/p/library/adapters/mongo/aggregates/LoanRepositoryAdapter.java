package pl.lodz.p.library.adapters.mongo.aggregates;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.lodz.p.library.adapters.mongo.documents.LoanDoc;
import pl.lodz.p.library.adapters.mongo.mappers.LoanMapper;
import pl.lodz.p.library.adapters.mongo.repositories.LoanRepository;
import pl.lodz.p.library.domain.model.Loan;
import pl.lodz.p.library.ports.outbound.LoanPort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class LoanRepositoryAdapter implements LoanPort {

    private final LoanRepository repository;
    private final LoanMapper mapper;

    @Autowired
    public LoanRepositoryAdapter(LoanRepository repository, LoanMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Loan> findById(String id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Loan> findAll() {
        return repository.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Loan> findByClientId(String clientId) {
        return repository.findByClientId(clientId).stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Loan> findByBookSetId(String bookSetId) {
        return repository.findByBookSetId(bookSetId).stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Loan> findByClientIdAndBookSetId(String clientId, String bookSetId) {
        return repository.findByClientIdAndBookSetId(clientId, bookSetId).stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Loan> findByActive(boolean active) {
        return repository.findByActive(active).stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Loan> findByClientIdAndActive(String clientId, boolean active) {
        return repository.findByClientIdAndActive(clientId, active).stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Loan> findByBookSetIdAndActive(String bookSetId, boolean active) {
        return repository.findByBookSetIdAndActive(bookSetId, active).stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<Loan> createLoan(String clientId, String bookSetId) {
        return this.createLoan(clientId, bookSetId, null);
    }

    @Override
    public Optional<Loan> createLoan(String clientId, String bookSetId, LocalDateTime startTime) {
        Loan loan = new Loan(clientId, bookSetId, startTime != null ? startTime : LocalDateTime.now());
        LoanDoc doc = mapper.toDocument(loan);
        LoanDoc saved = repository.save(doc);
        return Optional.ofNullable(mapper.toDomain(saved));
    }

    @Override
    public Optional<Loan> updateLoan(String loanId, Loan loanUpdates) {
        return repository.findById(loanId).map(existing -> {
            existing.setStartTime(loanUpdates.getStartTime());
            existing.setEndTime(loanUpdates.getEndTime());
            existing.setReturnTime(loanUpdates.getReturnTime());
            existing.setActive(loanUpdates.isActive());
            return mapper.toDomain(repository.save(existing));
        });
    }

    @Override
    public Optional<Loan> endLoan(String loanId) {
        return repository.findById(loanId).map(existing -> {
            existing.setActive(false);
            existing.setReturnTime(LocalDateTime.now());
            return mapper.toDomain(repository.save(existing));
        });
    }

    @Override
    public void deleteLoan(String id) {
        repository.deleteById(id);
    }
}