package pl.lodz.p.library.database;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.lodz.p.library.adapters.mongo.repositories.BookSetRepository;
import pl.lodz.p.library.adapters.mongo.repositories.LoanRepository;
import pl.lodz.p.library.adapters.mongo.repositories.UserRepository;
import pl.lodz.p.library.domain.model.*;
import pl.lodz.p.library.ports.inbound.BookSetUseCase;
import pl.lodz.p.library.ports.inbound.LoanUseCase;
import pl.lodz.p.library.ports.inbound.UserUseCase;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final BookSetRepository bookSetRepository;
    private final LoanRepository loanRepository;
    private final UserUseCase userUseCase;
    private final BookSetUseCase bookSetUseCase;
    private final LoanUseCase loanUseCase;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, BookSetRepository bookSetRepository, LoanRepository loanRepository, UserUseCase userUseCase, BookSetUseCase bookSetUseCase, LoanUseCase loanUseCase, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.bookSetRepository = bookSetRepository;
        this.loanRepository = loanRepository;
        this.userUseCase = userUseCase;
        this.bookSetUseCase = bookSetUseCase;
        this.loanUseCase = loanUseCase;
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
        Reader userKrzysztof = new Reader("krzysztof", passwordEncoder.encode("12345"), "krzysztof@gmail.com", 19);
        userJanek.setActive(true);
        userRemek.setActive(true);
        userPiotrek.setActive(true);
        userGrzegorz.setActive(true);
        userJanek = (Reader) userUseCase.addUser(userJanek);
        userRemek = (Reader) userUseCase.addUser(userRemek);
        userPiotrek = (Reader) userUseCase.addUser(userPiotrek);
        userGrzegorz = (Reader) userUseCase.addUser(userGrzegorz);
        userUseCase.addUser(userKrzysztof);

        Librarian libBarbara = new Librarian("lib.barbara", passwordEncoder.encode("12345"), "barbara@library.pl", 42);
        Librarian libTomasz = new Librarian("lib.tomasz", passwordEncoder.encode("12345"), "tomasz@library.pl", 51);
        libTomasz.setActive(true);
        userUseCase.addUser(libBarbara);
        userUseCase.addUser(libTomasz);

        Administrator admin = new Administrator("admin", passwordEncoder.encode("admin"), "admin@root.pl", 35);
        admin.setActive(true);
        userUseCase.addUser(admin);

        BookSet bookCoNas = new BookSet("Co nas nie zabije", "Wim Hof", 2017, 32);
        BookSet bookNicNas = new BookSet("Nic mnie nie złamie", "David Goggins", 2023, 10);
        BookSet bookAtomowe = new BookSet("Atomowe nawyki", "James Clear", 2019, 12);
        bookCoNas = bookSetUseCase.addBookSet(bookCoNas);
        bookNicNas = bookSetUseCase.addBookSet(bookNicNas);
        bookAtomowe = bookSetUseCase.addBookSet(bookAtomowe);

        Loan loanToEnd1 = loanUseCase.createLoan(userJanek.getId(), bookCoNas.getId());
        Loan loanToEnd2 = loanUseCase.createLoan(userRemek.getId(), bookNicNas.getId());
        Loan loanToEnd3 = loanUseCase.createLoan(userPiotrek.getId(), bookAtomowe.getId());

        loanUseCase.createLoan(userGrzegorz.getId(), bookAtomowe.getId());
        loanUseCase.createLoan(userJanek.getId(), bookNicNas.getId());
        loanUseCase.createLoan(userRemek.getId(), bookCoNas.getId());

        loanUseCase.endLoan(loanToEnd1.getId());
        loanUseCase.endLoan(loanToEnd2.getId());
        loanUseCase.endLoan(loanToEnd3.getId());
    }
}