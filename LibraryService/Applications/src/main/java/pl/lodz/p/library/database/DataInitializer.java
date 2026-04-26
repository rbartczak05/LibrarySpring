package pl.lodz.p.library.database;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.lodz.p.library.adapters.mongo.repositories.BookSetRepository;
import pl.lodz.p.library.adapters.mongo.repositories.LoanRepository;
import pl.lodz.p.library.domain.model.*;
import pl.lodz.p.library.ports.inbound.BookSetUseCase;
import pl.lodz.p.library.ports.inbound.LoanUseCase;
import pl.lodz.p.library.ports.outbound.ReaderPort;

import java.util.UUID;

@Component
public class DataInitializer implements CommandLineRunner {
    private final BookSetRepository bookSetRepository;
    private final LoanRepository loanRepository;
    private final BookSetUseCase bookSetUseCase;
    private final LoanUseCase loanUseCase;
    private final ReaderPort readerPort;

    public DataInitializer(BookSetRepository bookSetRepository, LoanRepository loanRepository, BookSetUseCase bookSetUseCase, LoanUseCase loanUseCase, ReaderPort readerPort) {
        this.bookSetRepository = bookSetRepository;
        this.loanRepository = loanRepository;
        this.bookSetUseCase = bookSetUseCase;
        this.loanUseCase = loanUseCase;
        this.readerPort = readerPort;
    }

    @Override
    public void run(String... args) {
        loanRepository.deleteAll();
        bookSetRepository.deleteAll();
        readerPort.deleteAll();

        Reader readerJanek = new Reader(UUID.randomUUID().toString(), "konto-uuid-janka", "Janek");
        Reader readerRemek = new Reader(UUID.randomUUID().toString(), "konto-uuid-remka", "Remek");
        Reader readerPiotrek = new Reader(UUID.randomUUID().toString(), "konto-uuid-piotrka", "Piotrek");

        readerJanek = readerPort.save(readerJanek);
        readerRemek = readerPort.save(readerRemek);
        readerPiotrek = readerPort.save(readerPiotrek);

        // 2. TWORZENIE KSIĄŻEK
        BookSet bookCoNas = new BookSet("Co nas nie zabije", "Wim Hof", 2017, 32);
        BookSet bookNicNas = new BookSet("Nic mnie nie złamie", "David Goggins", 2023, 10);
        BookSet bookAtomowe = new BookSet("Atomowe nawyki", "James Clear", 2019, 12);
        bookCoNas = bookSetUseCase.addBookSet(bookCoNas);
        bookNicNas = bookSetUseCase.addBookSet(bookNicNas);
        bookAtomowe = bookSetUseCase.addBookSet(bookAtomowe);

        // 3. TWORZENIE WYPOŻYCZEŃ (łączymy wewnętrzne ID profilu z ID Książki)
        Loan loanToEnd1 = loanUseCase.createLoan(readerJanek.getId(), bookCoNas.getId());
        Loan loanToEnd2 = loanUseCase.createLoan(readerRemek.getId(), bookNicNas.getId());
        Loan loanToEnd3 = loanUseCase.createLoan(readerPiotrek.getId(), bookAtomowe.getId());

        loanUseCase.createLoan(readerJanek.getId(), bookNicNas.getId());
        loanUseCase.createLoan(readerRemek.getId(), bookCoNas.getId());

        loanUseCase.endLoan(loanToEnd1.getId());
        loanUseCase.endLoan(loanToEnd2.getId());
        loanUseCase.endLoan(loanToEnd3.getId());

        System.out.println("LibraryService: Książki i wypożyczenia zostały pomyślnie zainicjowane.");
    }
}