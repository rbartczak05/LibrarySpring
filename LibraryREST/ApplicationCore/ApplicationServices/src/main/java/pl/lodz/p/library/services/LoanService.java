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
import pl.lodz.p.library.ports.outbound.*;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LoanService implements LoanUseCase {

    private final GetLoanPort getLoanPort;
    private final SaveLoanPort saveLoanPort;
    private final DeleteLoanPort deleteLoanPort;
    private final GetBookSetPort getBookSetPort;
    private final SaveBookSetPort saveBookSetPort;
    private final GetUserPort getUserPort;
    private final SaveUserPort saveUserPort;

    @Autowired
    public LoanService(GetLoanPort getLoanPort, SaveLoanPort saveLoanPort, DeleteLoanPort deleteLoanPort,
                       GetBookSetPort getBookSetPort, SaveBookSetPort saveBookSetPort,
                       GetUserPort getUserPort, SaveUserPort saveUserPort) {
        this.getLoanPort = getLoanPort;
        this.saveLoanPort = saveLoanPort;
        this.deleteLoanPort = deleteLoanPort;
        this.getBookSetPort = getBookSetPort;
        this.saveBookSetPort = saveBookSetPort;
        this.getUserPort = getUserPort;
        this.saveUserPort = saveUserPort;
    }

    public Loan findLoanById(String id) {
        return getLoanPort.findById(id)
                .orElseThrow(() -> new LoanException("Wypożyczenie o ID: " + id + " nie zostało odnalezione."));
    }

    public List<Loan> findLoansByReader(String readerId) {
        return getLoanPort.findByReaderId(readerId);
    }

    public List<Loan> findLoansByBookSet(String bookSetId) {
        return getLoanPort.findByBookSetId(bookSetId);
    }

    public List<Loan> findLoansByReaderIdAndBookSetId(String readerId, String bookSetId) {
        return getLoanPort.findByReaderIdAndBookSetId(readerId, bookSetId);
    }

    public List<Loan> findByActiveLoans(boolean active) {
        return getLoanPort.findByActive(active);
    }

    public List<Loan> findByReaderIdAndActive(String readerId, boolean active) {
        return getLoanPort.findByReaderIdAndActive(readerId, active);
    }

    public List<Loan> findByBookSetIdAndActive(String bookSetId, boolean active) {
        return getLoanPort.findByBookSetIdAndActive(bookSetId, active);
    }

    public List<Loan> findAllLoans() {
        return getLoanPort.findAll();
    }

    @Transactional
    public Loan createLoan(String readerId, String bookSetId) {
        return createLoan(readerId, bookSetId, null);
    }

    @Transactional
    public Loan createLoan(String readerId, String bookSetId, LocalDateTime loanStartTime) {
        Reader reader = (Reader) getUserPort.findUserById(readerId).orElseThrow(() -> new UserException("User not found"));
        BookSet bookSet = getBookSetPort.findById(bookSetId).orElseThrow(() -> new BookSetException("BookSet not found"));

        if (!reader.canBorrowBook()) {
            throw new UserException("Reader with id: " + readerId + " cannot borrow loan.");
        }
        if (!bookSet.isAvailable()) {
            throw new BookSetException("BookSet with id " + bookSetId + " is not available for loan.");
        }

        bookSet.setQuantity(bookSet.getQuantity() - 1);
        saveBookSetPort.save(bookSet);

        reader.setCurrentLoansCount(reader.getCurrentLoansCount() + 1);
        saveUserPort.addUser(reader);

        return saveLoanPort.createLoan(readerId, bookSetId, loanStartTime)
                .orElseThrow(() -> new LoanException("Nie udało się utworzyć wypożyczenia."));
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

        return saveLoanPort.updateLoan(loanId, loanUpdates)
                .orElseThrow(() -> new LoanException("Nie udało się zaktualizować wypożyczenia."));
    }

    @Transactional
    public Loan endLoan(String loanId) {
        Loan loan = findLoanById(loanId);

        if (!loan.isActive()) {
            throw new LoanException("Wypożyczenie zostało już zakończone.");
        }

        BookSet bookSet = getBookSetPort.findById(loan.getBookSetId()).orElseThrow(() -> new BookSetException("BookSet not found"));
        bookSet.setQuantity(bookSet.getQuantity() + 1);
        saveBookSetPort.save(bookSet);

        Reader reader = (Reader) getUserPort.findUserById(loan.getReaderId()).orElseThrow(() -> new UserException("User not found"));
        reader.setCurrentLoansCount(reader.getCurrentLoansCount() - 1);
        saveUserPort.addUser(reader);

        return saveLoanPort.endLoan(loanId)
                .orElseThrow(() -> new LoanException("Nie udało się zakończyć wypożyczenia."));
    }

    @Transactional
    public void deleteLoan(String loanId) {
        Loan loan = findLoanById(loanId);
        if (loan.isActive()) {
            throw new LoanException("Nie można usunąć aktywnego wypożyczenia.");
        }
        deleteLoanPort.deleteLoan(loanId);
    }
}