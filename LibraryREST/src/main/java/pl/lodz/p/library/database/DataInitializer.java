package pl.lodz.p.library.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.lodz.p.library.model.*;
import pl.lodz.p.library.repository.BookSetRepository;
import pl.lodz.p.library.repository.LoanRepository;
import pl.lodz.p.library.repository.UserRepository;
import pl.lodz.p.library.service.LoanService;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BookSetRepository bookSetRepository;
    private final LoanRepository loanRepository;
    private final LoanService loanService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(UserRepository userRepository, BookSetRepository bookSetRepository, LoanRepository loanRepository, LoanService loanService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.bookSetRepository = bookSetRepository;
        this.loanRepository = loanRepository;
        this.loanService = loanService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        loanRepository.deleteAll();
        userRepository.deleteAll();
        bookSetRepository.deleteAll();

        Reader userJanek = new Reader("janek", passwordEncoder.encode("12345"), "janek@gmail.com", 33);
        Reader userRemek = new Reader("remek", passwordEncoder.encode("12345"), "remek@wp.pl", 22);
        Reader userPiotrek = new Reader("piotrek", passwordEncoder.encode("12345"), "piotrek@interia.pl", 22);
        Reader userGrzegorz = new Reader("grzegorz", passwordEncoder.encode("12345"), "grzegorz@gmail.com", 45);
        Reader userKrzysztof = new Reader("krzysztof", passwordEncoder.encode("12345"), "krzysztof@gmail.com", 19); // nieaktywny
        userJanek.setActive(true);
        userRemek.setActive(true);
        userPiotrek.setActive(true);
        userGrzegorz.setActive(true);
        userRepository.saveAll(List.of(userJanek, userRemek, userPiotrek, userGrzegorz, userKrzysztof));

        Librarian libBarbara = new Librarian("lib.barbara", passwordEncoder.encode("12345"), "barbara@library.pl", 42);
        Librarian libTomasz = new Librarian("lib.tomasz", passwordEncoder.encode("12345"), "tomasz@library.pl", 51);
        userRepository.saveAll(List.of(libBarbara, libTomasz));

        Administrator admin = new Administrator("admin", passwordEncoder.encode("admin"), "admin@root.pl", 35);
        userRepository.save(admin);

        BookSet bookCoNas = new BookSet("Co nas nie zabije", "Wim Hof", 2017, 32);
        BookSet bookNicNas = new BookSet("Nic mnie nie złamie", "David Goggins", 2023, 10);
        BookSet bookAtomowe = new BookSet("Atomowe nawyki", "James Clear", 2019, 12);
        bookSetRepository.saveAll(List.of(bookCoNas, bookNicNas, bookAtomowe));

        Loan loanToEnd1 = loanService.createLoan(userJanek.getId(), bookCoNas.getId());
        Loan loanToEnd2 = loanService.createLoan(userRemek.getId(), bookNicNas.getId());
        Loan loanToEnd3 = loanService.createLoan(userPiotrek.getId(), bookAtomowe.getId());

        loanService.createLoan(userGrzegorz.getId(), bookAtomowe.getId());
        loanService.createLoan(userJanek.getId(), bookNicNas.getId());
        loanService.createLoan(userRemek.getId(), bookCoNas.getId());

        loanService.endLoan(loanToEnd1.getId());
        loanService.endLoan(loanToEnd2.getId());
        loanService.endLoan(loanToEnd3.getId());
    }
}