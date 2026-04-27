package pl.lodz.p.library.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.library.domain.exceptions.BookSetException;
import pl.lodz.p.library.domain.exceptions.ClientException;
import pl.lodz.p.library.domain.exceptions.LoanException;
import pl.lodz.p.library.domain.model.BookSet;
import pl.lodz.p.library.domain.model.Client;
import pl.lodz.p.library.domain.model.Loan;
import pl.lodz.p.library.ports.inbound.LoanUseCase;
import pl.lodz.p.library.ports.outbound.BookSetPort;
import pl.lodz.p.library.ports.outbound.ClientPort;
import pl.lodz.p.library.ports.outbound.LoanPort;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LoanService implements LoanUseCase {

    private final LoanPort loanPort;
    private final BookSetPort bookSetPort;
    private final ClientPort clientPort;

    @Autowired
    public LoanService(LoanPort loanPort, BookSetPort bookSetPort, ClientPort clientPort) {
        this.loanPort = loanPort;
        this.bookSetPort = bookSetPort;
        this.clientPort = clientPort;
    }

    public Loan findLoanById(String id) {
        return loanPort.findById(id).orElseThrow(() -> new LoanException("Wypożyczenie o ID: " + id + " nie zostało odnalezione."));
    }

    public List<Loan> findByClientId(String clientId) {
        return loanPort.findByClientId(clientId);
    }

    public List<Loan> findByBookSetId(String bookSetId) {
        return loanPort.findByBookSetId(bookSetId);
    }

    public List<Loan> findByClientIdAndBookSetId(String clientId, String bookSetId) {
        return loanPort.findByClientIdAndBookSetId(clientId, bookSetId);
    }

    public List<Loan> findByActiveLoans(boolean active) {
        return loanPort.findByActive(active);
    }

    public List<Loan> findByClientIdAndActive(String clientId, boolean active) {
        return loanPort.findByClientIdAndActive(clientId, active);
    }

    public List<Loan> findByBookSetIdAndActive(String bookSetId, boolean active) {
        return loanPort.findByBookSetIdAndActive(bookSetId, active);
    }

    public List<Loan> findAllLoans() {
        return loanPort.findAll();
    }

    @Transactional
    public Loan createLoan(String clientId, String bookSetId) {
        return createLoan(clientId, bookSetId, null);
    }

    @Transactional
    public Loan createLoan(String clientId, String bookSetId, LocalDateTime loanStartTime) {
        Client client = clientPort.findById(clientId).orElseThrow(() -> new ClientException("Client not found"));
        BookSet bookSet = bookSetPort.findById(bookSetId).orElseThrow(() -> new BookSetException("BookSet not found"));

        if (!client.canBorrowBook()) {
            throw new ClientException("Client with id: " + clientId + " cannot borrow loan.");
        }
        if (!bookSet.isAvailable()) {
            throw new BookSetException("BookSet with id " + bookSetId + " is not available for loan.");
        }

        bookSet.setQuantity(bookSet.getQuantity() - 1);
        bookSetPort.save(bookSet);

        client.setCurrentLoansCount(client.getCurrentLoansCount() + 1);
        clientPort.save(client);

        return loanPort.createLoan(clientId, bookSetId, loanStartTime).orElseThrow(() -> new LoanException("Nie udało się utworzyć wypożyczenia."));
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

        Client client = clientPort.findById(loan.getClientId()).orElseThrow(() -> new ClientException("Client not found"));
        client.setCurrentLoansCount(client.getCurrentLoansCount() - 1);
        clientPort.save(client);

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