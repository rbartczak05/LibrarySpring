package pl.lodz.p.library.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.lodz.p.library.converter.UserConverter;
import pl.lodz.p.library.dto.LibrarianDTO;
import pl.lodz.p.library.model.Librarian;
import pl.lodz.p.library.model.User;
import pl.lodz.p.library.security.JwtService;
import pl.lodz.p.library.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/librarians")
public class LibrarianController {

    private final UserService userService;
    private final JwtService jwtService;

    @Autowired
    public LibrarianController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @GetMapping
    public List<LibrarianDTO> getAllLibrarians() {
        return userService.findAllUsers().stream()
                .filter(Librarian.class::isInstance)
                .map(Librarian.class::cast)
                .map(UserConverter::toLibrarianDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public LibrarianDTO getLibrarianById(@PathVariable String id) {
        User user = userService.findUserById(id);
        LibrarianDTO librarianDTO = UserConverter.toLibrarianDTO((Librarian) user);

        String signature = jwtService.generateSignatureForId(id);

        return ResponseEntity.ok()
                .header("If-Match", signature)
                .body(librarianDTO).getBody();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LibrarianDTO addLibrarian(@Valid @RequestBody LibrarianDTO librarianDTO) {
        Librarian toSave = UserConverter.fromLibrarianDTO(librarianDTO);
        return UserConverter.toLibrarianDTO((Librarian) userService.addUser(toSave));
    }
}