package pl.lodz.p.library.adapters.mongo.aggregates;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.lodz.p.library.adapters.mongo.documents.LoanDoc;
import pl.lodz.p.library.adapters.mongo.mappers.LoanMapper;
import pl.lodz.p.library.adapters.mongo.repositories.LoanRepository;
import pl.lodz.p.library.domain.model.Loan;
import pl.lodz.p.library.ports.outbound.DeleteLoanPort;
import pl.lodz.p.library.ports.outbound.GetLoanPort;
import pl.lodz.p.library.ports.outbound.SaveLoanPort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class LoanRepositoryAdapter implements GetLoanPort, SaveLoanPort, DeleteLoanPort {

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
    public List<Loan> findByReaderId(String readerId) {
        return repository.findByReaderId(readerId).stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Loan> findByBookSetId(String bookSetId) {
        return repository.findByBookSetId(bookSetId).stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Loan> findByReaderIdAndBookSetId(String readerId, String bookSetId) {
        return repository.findByReaderIdAndBookSetId(readerId, bookSetId).stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Loan> findByActive(boolean active) {
        return repository.findByActive(active).stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Loan> findByBookSetIdAndActive(String bookSetId, boolean active) {
        return repository.findByBookSetIdAndActive(bookSetId, active).stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Loan> findByReaderIdAndActive(String readerId, boolean active) {
        return repository.findByReaderIdAndActive(readerId, active).stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Loan> findAll() {
        return repository.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<Loan> createLoan(String readerId, String bookSetId) {
        LoanDoc doc = new LoanDoc(readerId, bookSetId, LocalDateTime.now());
        return Optional.ofNullable(mapper.toDomain(repository.save(doc)));
    }

    @Override
    public Optional<Loan> createLoan(String readerId, String bookSetId, LocalDateTime loanStartTime) {
        LoanDoc doc = new LoanDoc(readerId, bookSetId, loanStartTime != null ? loanStartTime : LocalDateTime.now());
        return Optional.ofNullable(mapper.toDomain(repository.save(doc)));
    }

    @Override
    public Optional<Loan> updateLoan(String loanId, Loan loanUpdates) {
        return repository.findById(loanId).map(existing -> {
            existing.setStartTime(loanUpdates.getStartTime());
            existing.setEndTime(loanUpdates.getEndTime());
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
    public void deleteLoan(String loanId) {
        repository.deleteById(loanId);
    }
}