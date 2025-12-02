package pl.lodz.p.library.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.lodz.p.library.converter.UserConverter;
import pl.lodz.p.library.dto.LibrarianDTO;
import pl.lodz.p.library.model.Librarian;
import pl.lodz.p.library.model.User;
import pl.lodz.p.library.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/librarians")
public class LibrarianController {

    private final UserService userService;

    @Autowired
    public LibrarianController(UserService userService) {
        this.userService = userService;
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
        if (!(user instanceof Librarian)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User is not a Librarian");
        }
        return UserConverter.toLibrarianDTO((Librarian) user);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LibrarianDTO addLibrarian(@Valid @RequestBody LibrarianDTO librarianDTO) {
        Librarian toSave = UserConverter.fromLibrarianDTO(librarianDTO);
        return UserConverter.toLibrarianDTO((Librarian) userService.addUser(toSave));
    }
}