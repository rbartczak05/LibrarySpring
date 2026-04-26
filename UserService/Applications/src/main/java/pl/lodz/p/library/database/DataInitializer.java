package pl.lodz.p.library.database;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.lodz.p.library.adapters.mongo.repositories.UserRepository;
import pl.lodz.p.library.domain.model.*;
import pl.lodz.p.library.ports.inbound.UserUseCase;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final UserUseCase userUseCase;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, UserUseCase userUseCase, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userUseCase = userUseCase;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        userRepository.deleteAll();

        Reader userJanek = new Reader("janek", passwordEncoder.encode("12345"), "janek@gmail.com", "Jan", "Kowalski", 33);
        Reader userRemek = new Reader("remek", passwordEncoder.encode("12345"), "remek@wp.pl", "Remigiusz", "Nowak", 22);
        Reader userPiotrek = new Reader("piotrek", passwordEncoder.encode("12345"), "piotrek@interia.pl", "Piotr", "Wiśniewski", 22);
        Reader userGrzegorz = new Reader("grzegorz", passwordEncoder.encode("12345"), "grzegorz@gmail.com", "Grzegorz", "Brzęczyszczykiewicz", 45);

        userJanek.setActive(true);
        userRemek.setActive(true);
        userPiotrek.setActive(true);
        userGrzegorz.setActive(true);

        userUseCase.addUser(userJanek);
        userUseCase.addUser(userRemek);
        userUseCase.addUser(userPiotrek);
        userUseCase.addUser(userGrzegorz);

        Librarian libBarbara = new Librarian("lib.barbara", passwordEncoder.encode("12345"), "barbara@library.pl", "Barbara", "Bibliotekarz", 42);
        Librarian libTomasz = new Librarian("lib.tomasz", passwordEncoder.encode("12345"), "tomasz@library.pl", "Tomasz", "Książka", 51);
        libTomasz.setActive(true);
        userUseCase.addUser(libBarbara);
        userUseCase.addUser(libTomasz);

        Administrator admin = new Administrator("admin", passwordEncoder.encode("admin"), "admin@root.pl", "Adam", "Administrator", 35);
        admin.setActive(true);
        userUseCase.addUser(admin);

        System.out.println("UserService: Konta użytkowników zostały pomyślnie zainicjowane.");
    }
}