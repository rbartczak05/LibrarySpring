package pl.lodz.p.library.migrations;

import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import pl.lodz.p.library.adapters.mongo.documents.BookSetDoc;
import pl.lodz.p.library.adapters.mongo.documents.ClientDoc;
import pl.lodz.p.library.adapters.mongo.repositories.BookSetRepository;
import pl.lodz.p.library.adapters.mongo.repositories.ClientRepository;

import java.util.UUID;

@ChangeUnit(id = "setup-library-data", order = "001", author = "system")
public class InitialLibrarySetup {

    @Execution
    public void execution(BookSetRepository bookRepo, ClientRepository clientRepo) {

        ClientDoc jan = new ClientDoc("Jan", "Nowak", "jan.nowak@example.com", 25);
        jan.setId(UUID.fromString("e976b5e1-cf61-4daf-9c2f-9979f6b54015"));
        jan.setActive(true);
        clientRepo.save(jan);

        BookSetDoc book1 = new BookSetDoc("Wiedźmin: Miecz Przeznaczenia", "Andrzej Sapkowski", 1992, 10);
        book1.setId(UUID.randomUUID());
        bookRepo.save(book1);

        BookSetDoc book2 = new BookSetDoc("Atomowe nawyki", "James Clear", 2018, 15);
        book2.setId(UUID.randomUUID());
        bookRepo.save(book2);

        BookSetDoc book3 = new BookSetDoc("7 nawyków skutecznego działania", "Stephen R. Covey", 1989, 5);
        book3.setId(UUID.randomUUID());
        bookRepo.save(book3);

        BookSetDoc book4 = new BookSetDoc("Myśl i bogać się", "Napoleon Hill", 1937, 8);
        book4.setId(UUID.randomUUID());
        bookRepo.save(book4);

        BookSetDoc book5 = new BookSetDoc("Potęga podświadomości", "Joseph Murphy", 1963, 12);
        book5.setId(UUID.randomUUID());
        bookRepo.save(book5);
    }

    @RollbackExecution
    public void rollback() {
    }
}