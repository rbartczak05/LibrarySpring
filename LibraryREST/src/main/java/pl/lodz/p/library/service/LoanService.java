package pl.lodz.p.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.library.exception.*;
import pl.lodz.p.library.model.BookSet;
import pl.lodz.p.library.model.Loan;
import pl.lodz.p.library.model.Reader;
import pl.lodz.p.library.repository.BookSetRepository;
import pl.lodz.p.library.repository.LoanRepository;
import pl.lodz.p.library.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookSetRepository bookSetRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final BookSetService bookSetService;

    @Autowired
    public LoanService(LoanRepository loanRepository, BookSetRepository bookSetRepository, UserRepository userRepository, UserService userService, BookSetService bookSetService) {
        this.loanRepository = loanRepository;
        this.bookSetRepository = bookSetRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.bookSetService = bookSetService;
    }

    public Loan findLoanById(String id) {
        return loanRepository.findById(id)
                .orElseThrow(() -> new LoanNotFoundException(HttpStatus.NOT_FOUND, "Loan with id: " + id + " not found."));
    }

    public List<Loan> findLoansByReader(String readerId) {
        return loanRepository.findByReaderId(readerId);
    }

    public List<Loan> findLoansByBookSet(String bookSetId) {
        return loanRepository.findByBookSetId(bookSetId);
    }

    public List<Loan> findLoansByReaderIdAndBookSetId(String readerId, String bookSetId) {
        return loanRepository.findByReaderIdAndBookSetId(readerId, bookSetId);
    }

    public List<Loan> findByActiveLoans(boolean active) {
        return loanRepository.findByActive(active);
    }

    public List<Loan> findByReaderIdAndActive(String readerId, boolean active) {
        return loanRepository.findByReaderIdAndActive(readerId, active);
    }

    public List<Loan> findByBookSetIdAndActive(String bookSetId, boolean active) {
        return loanRepository.findByBookSetIdAndActive(bookSetId, active);
    }

    public List<Loan> findAllLoans() {
        return loanRepository.findAll();
    }

    // Gdy czas początkowy nie jest określany wcale
    @Transactional
    public Loan createLoan(String readerId, String bookSetId) {
        return createLoan(readerId, bookSetId, null);
    }

    // Do ustalania tego czasu początkowego wypożyczenia
    @Transactional
    public Loan createLoan(String readerId, String bookSetId, LocalDateTime loanStartTime) {
        Reader reader = (Reader) userService.findUserById(readerId);
        BookSet bookSet = bookSetService.findBookSetById(bookSetId);

        if (reader == null) {
            throw new ReaderNotFoundException(HttpStatus.NOT_FOUND, "Loan with id: " + readerId + " not found.");
        }
        if (bookSet == null) {
            throw new BookSetNotFoundException(HttpStatus.NOT_FOUND, "BookSet with id: " + bookSetId + " not found.");
        }
        if (!reader.canBorrowBook()) {
            throw new ReaderLimitsException(HttpStatus.CONFLICT, "Reader with id: " + readerId + " cannot borrow loan.");
        }
        if (!bookSet.isAvailable()) {
            throw new BookSetNotAvailableException(HttpStatus.CONFLICT, "BookSet with id " + bookSetId + " is not available for loan.");
        }

        bookSet.setQuantity(bookSet.getQuantity() - 1);
        bookSetRepository.save(bookSet);

        reader.setCurrentLoansCount(reader.getCurrentLoansCount() + 1);
        userRepository.save(reader);

        // Gdyby loanStartTime był null to jest ustawiany na LocalDateTime.now()
        LocalDateTime actualStartTime = (loanStartTime != null) ? loanStartTime : LocalDateTime.now();
        Loan loan = new Loan(readerId, bookSetId, actualStartTime);

        return loanRepository.save(loan);
    }

    @Transactional
    public Loan updateLoan(String loanId, Loan loanUpdates) {
        if (loanUpdates == null) {
            throw new LoanNotFoundException(HttpStatus.NOT_FOUND, "LoanUpdates not found.");
        }

        if (loanRepository.findById(loanId).isEmpty()) {
            throw new LoanNotFoundException(HttpStatus.NOT_FOUND, "Loan with id: " + loanId + " not found.");
        }

        Loan existingLoan = findLoanById(loanId);

        if (!existingLoan.isActive()) {
            throw new LoanAlreadyInactiveException(HttpStatus.BAD_REQUEST, "Loan is already ended.");
        }

        // Zmiana czasu początkowego i końcowego wypożyczenia wydaje się mieć jakiś seks skoro mamy konstruktor,
        // który ustala początek czasu przykładowo z przyszłości. Też do skrócenia czasu oddania książki (?)
        existingLoan.setStartTime(loanUpdates.getStartTime());
        existingLoan.setEndTime(loanUpdates.getEndTime());

        return loanRepository.save(existingLoan);
    }

    @Transactional
    public Loan endLoan(String loanId) {
        if (loanRepository.findById(loanId).isEmpty()) {
            throw new LoanNotFoundException(HttpStatus.NOT_FOUND, "Loan with id: " + loanId + " not found.");
        }

        Loan loan = findLoanById(loanId);

        if (!loan.isActive()) {
            throw new LoanAlreadyInactiveException(HttpStatus.BAD_REQUEST, "Loan is already ended.");
        }

        loan.setActive(false);
        loan.setReturnTime(LocalDateTime.now());

        BookSet bookSet = bookSetService.findBookSetById(loan.getBookSetId());
        bookSet.setQuantity(bookSet.getQuantity() + 1);
        bookSetRepository.save(bookSet);

        Reader reader = (Reader) userService.findUserById(loan.getReaderId());
        reader.setCurrentLoansCount(reader.getCurrentLoansCount() - 1);
        userRepository.save(reader);

        return loanRepository.save(loan);
    }

    /// Zachowane, żeby w razie czego mieć taką funkcję i udostępniać całego CRU(D)-a w aplikacji dla wypożyczenia.
//    @Transactional
//    public void deleteLoan(String loanId) {
//        if(loanRepository.findById(loanId).isEmpty()) {
//            throw new LoanNotFoundException(HttpStatus.NOT_FOUND, "Loan with id: " + loanId + " not found.");
//        }
//
//        Loan loan = findLoanById(loanId);
//
//        if (loan.isActive()) {
//            throw new LoanAlreadyInactiveException(HttpStatus.BAD_REQUEST, "Cannot delete active Loan.");
//        }
//
//        loanRepository.delete(loan);
//    }
}

