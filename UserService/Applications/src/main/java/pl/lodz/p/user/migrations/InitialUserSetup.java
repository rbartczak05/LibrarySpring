package pl.lodz.p.user.migrations;

import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.lodz.p.user.adapters.mongo.documents.AdministratorDoc;
import pl.lodz.p.user.adapters.mongo.documents.LibrarianDoc;
import pl.lodz.p.user.adapters.mongo.documents.ReaderDoc;
import pl.lodz.p.user.adapters.mongo.repositories.UserRepository;

import java.util.UUID;

@ChangeUnit(id = "setup-initial-users", order = "001", author = "system")
public class InitialUserSetup {

    @Execution
    public void execution(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        AdministratorDoc admin = new AdministratorDoc(
                "admin", passwordEncoder.encode("admin123!"), "admin@root.pl", "Adam", "Administrator", 35, true);
        admin.setId(UUID.randomUUID());
        userRepository.save(admin);

        LibrarianDoc librarian = new LibrarianDoc(
                "bibliotekarz", passwordEncoder.encode("biblio123!"), "biblioteka@example.com", "Anna", "Kowalska", 40, true);
        librarian.setId(UUID.randomUUID());
        userRepository.save(librarian);

        ReaderDoc reader1 = new ReaderDoc(
                "jan_nowak", passwordEncoder.encode("Password123!"), "jan.nowak@example.com", "Jan", "Nowak", 25, true);
        reader1.setId(UUID.fromString("e976b5e1-cf61-4daf-9c2f-9979f6b54015"));
        userRepository.save(reader1);

        ReaderDoc reader2 = new ReaderDoc(
                "piotr_z", passwordEncoder.encode("Piotr123!"), "piotr.z@example.com", "Piotr", "Zieliński", 30, true);
        reader2.setId(UUID.randomUUID());
        userRepository.save(reader2);
    }

    @RollbackExecution
    public void rollback() {

    }
}