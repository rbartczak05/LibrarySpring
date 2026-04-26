package pl.lodz.p.library.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.library.domain.exceptions.BookSetException;
import pl.lodz.p.library.domain.exceptions.LoanException;
import pl.lodz.p.library.domain.exceptions.UserException;
import pl.lodz.p.library.domain.model.BookSet;
import pl.lodz.p.library.domain.model.Loan;
import pl.lodz.p.library.domain.model.Reader;
import pl.lodz.p.library.ports.inbound.LoanUseCase;
import pl.lodz.p.library.ports.outbound.BookSetPort;
import pl.lodz.p.library.ports.outbound.LoanPort;
import pl.lodz.p.library.ports.outbound.UserPort;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LoanService implements LoanUseCase {

    private final LoanPort loanPort;
    private final BookSetPort bookSetPort;
    private final UserPort userPort;

    @Autowired
    public LoanService(LoanPort loanPort, BookSetPort bookSetPort, UserPort userPort) {

        this.loanPort = loanPort;
        this.bookSetPort = bookSetPort;
        this.userPort = userPort;
    }

    public Loan findLoanById(String id) {
        return loanPort.findById(id).orElseThrow(() -> new LoanException("Wypożyczenie o ID: " + id + " nie zostało odnalezione."));
    }

    public List<Loan> findLoansByReader(String readerId) {
        return loanPort.findByReaderId(readerId);
    }

    public List<Loan> findLoansByBookSet(String bookSetId) {
        return loanPort.findByBookSetId(bookSetId);
    }

    public List<Loan> findLoansByReaderIdAndBookSetId(String readerId, String bookSetId) {
        return loanPort.findByReaderIdAndBookSetId(readerId, bookSetId);
    }

    public List<Loan> findByActiveLoans(boolean active) {
        return loanPort.findByActive(active);
    }

    public List<Loan> findByReaderIdAndActive(String readerId, boolean active) {
        return loanPort.findByReaderIdAndActive(readerId, active);
    }

    public List<Loan> findByBookSetIdAndActive(String bookSetId, boolean active) {
        return loanPort.findByBookSetIdAndActive(bookSetId, active);
    }

    public List<Loan> findAllLoans() {
        return loanPort.findAll();
    }

    @Transactional
    public Loan createLoan(String readerId, String bookSetId) {
        return createLoan(readerId, bookSetId, null);
    }

    @Transactional
    public Loan createLoan(String readerId, String bookSetId, LocalDateTime loanStartTime) {
        Reader reader = (Reader) userPort.findUserById(readerId).orElseThrow(() -> new UserException("User not found"));
        BookSet bookSet = bookSetPort.findById(bookSetId).orElseThrow(() -> new BookSetException("BookSet not found"));

        if (!reader.canBorrowBook()) {
            throw new UserException("Reader with id: " + readerId + " cannot borrow loan.");
        }
        if (!bookSet.isAvailable()) {
            throw new BookSetException("BookSet with id " + bookSetId + " is not available for loan.");
        }

        bookSet.setQuantity(bookSet.getQuantity() - 1);
        bookSetPort.save(bookSet);

        reader.setCurrentLoansCount(reader.getCurrentLoansCount() + 1);
        userPort.addUser(reader);

        return loanPort.createLoan(readerId, bookSetId, loanStartTime).orElseThrow(() -> new LoanException("Nie udało się utworzyć wypożyczenia."));
    }

    @Transactional
    public Loan updateLoan(String loanId, Loan loanUpdates) {
        if (loanUpdates == null) {
            throw new LoanException("Zmiany wypożyczenia nie zostały odnalezione.");
        }
        Loan existingLoan = findLoanById(loanId);

        if (!existingLoan.isActive()) {
            throw new LoanException("Wypożyczenie zostało już zakończone.");
        }

        return loanPort.updateLoan(loanId, loanUpdates).orElseThrow(() -> new LoanException("Nie udało się zaktualizować wypożyczenia."));
    }

    @Transactional
    public Loan endLoan(String loanId) {
        Loan loan = findLoanById(loanId);

        if (!loan.isActive()) {
            throw new LoanException("Wypożyczenie zostało już zakończone.");
        }

        BookSet bookSet = bookSetPort.findById(loan.getBookSetId()).orElseThrow(() -> new BookSetException("BookSet not found"));
        bookSet.setQuantity(bookSet.getQuantity() + 1);
        bookSetPort.save(bookSet);

        Reader reader = (Reader) userPort.findUserById(loan.getReaderId()).orElseThrow(() -> new UserException("User not found"));
        reader.setCurrentLoansCount(reader.getCurrentLoansCount() - 1);
        userPort.addUser(reader);

        return loanPort.endLoan(loanId).orElseThrow(() -> new LoanException("Nie udało się zakończyć wypożyczenia."));
    }

    @Transactional
    public void deleteLoan(String loanId) {
        Loan loan = findLoanById(loanId);
        if (loan.isActive()) {
            throw new LoanException("Nie można usunąć aktywnego wypożyczenia.");
        }
        loanPort.deleteLoan(loanId);
    }
}