package pl.lodz.p.library.database;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.lodz.p.library.domain.model.BookSet;
import pl.lodz.p.library.domain.model.Client;
import pl.lodz.p.library.ports.inbound.BookSetUseCase;
import pl.lodz.p.library.ports.inbound.LoanUseCase;
import pl.lodz.p.library.ports.outbound.ClientPort;

@Component
public class DataInitializer implements CommandLineRunner {
    private final BookSetUseCase bookSetUseCase;
    private final LoanUseCase loanUseCase;
    private final ClientPort clientPort;

    public DataInitializer(BookSetUseCase bookSetUseCase, LoanUseCase loanUseCase, ClientPort clientPort) {
        this.bookSetUseCase = bookSetUseCase;
        this.loanUseCase = loanUseCase;
        this.clientPort = clientPort;
    }

    @Override
    public void run(String... args) {
        Client jan = new Client("Jan", "Kowalski", "jan@test.pl", 25);
        jan.setActive(true);
        jan = clientPort.save(jan);

        Client anna = new Client("Anna", "Nowak", "anna@test.pl", 30);
        anna.setActive(true);
        anna = clientPort.save(anna);

        BookSet book1 = new BookSet("Wiedźmin", "Andrzej Sapkowski", 1993, 10);
        BookSet book2 = new BookSet("Solaris", "Stanisław Lem", 1961, 5);
        book1 = bookSetUseCase.addBookSet(book1);
        book2 = bookSetUseCase.addBookSet(book2);

        loanUseCase.createLoan(jan.getId(), book1.getId());
        loanUseCase.createLoan(anna.getId(), book2.getId());
    }
}