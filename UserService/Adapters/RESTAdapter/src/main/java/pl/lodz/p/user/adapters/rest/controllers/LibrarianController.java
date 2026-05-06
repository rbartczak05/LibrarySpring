package pl.lodz.p.user.adapters.rest.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.user.adapters.rest.converters.UserConverter;
import pl.lodz.p.user.adapters.rest.dto.LibrarianDTO;
import pl.lodz.p.user.adapters.rest.security.JwtService;
import pl.lodz.p.user.domain.model.Librarian;
import pl.lodz.p.user.domain.model.User;
import pl.lodz.p.user.ports.inbound.UserUseCase;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/librarians")
public class LibrarianController {
    private final UserUseCase userUseCase;
    private final JwtService jwtService;

    public LibrarianController(UserUseCase userUseCase, JwtService jwtService) {
        this.userUseCase = userUseCase;
        this.jwtService = jwtService;
    }

    @GetMapping
    public List<LibrarianDTO> getAllLibrarians() {
        return userUseCase.findAllUsers().stream()
                .filter(Librarian.class::isInstance)
                .map(Librarian.class::cast)
                .map(UserConverter::toLibrarianDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LibrarianDTO> getLibrarianById(@PathVariable UUID id) {
        User user = userUseCase.findUserById(id);
        LibrarianDTO librarianDTO = UserConverter.toLibrarianDTO((Librarian) user);
        String signature = jwtService.generateSignatureForId(id);
        return ResponseEntity.ok()
                .header("If-Match", signature)
                .body(librarianDTO);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LibrarianDTO addLibrarian(@Valid @RequestBody LibrarianDTO librarianDTO) {
        Librarian toSave = UserConverter.fromLibrarianDTO(librarianDTO);
        return UserConverter.toLibrarianDTO((Librarian) userUseCase.addUser(toSave));
    }
}