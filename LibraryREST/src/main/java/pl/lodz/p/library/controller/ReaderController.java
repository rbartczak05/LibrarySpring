package pl.lodz.p.library.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.lodz.p.library.converter.UserConverter;
import pl.lodz.p.library.dto.ReaderDTO;
import pl.lodz.p.library.model.Reader;
import pl.lodz.p.library.model.User;
import pl.lodz.p.library.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/readers")
public class ReaderController {

    private final UserService userService;

    @Autowired
    public ReaderController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<ReaderDTO> getAllReaders() {
        return userService.findAllUsers().stream()
                .filter(Reader.class::isInstance)
                .map(Reader.class::cast)
                .map(UserConverter::toReaderDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ReaderDTO getReaderById(@PathVariable String id) {
        User user = userService.findUserById(id);
        if (!(user instanceof Reader)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User found but is not a Reader");
        }
        return UserConverter.toReaderDTO((Reader) user);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReaderDTO addReader(@Valid @RequestBody ReaderDTO readerDTO) {
        Reader toSave = UserConverter.fromReaderDTO(readerDTO);
        return UserConverter.toReaderDTO((Reader) userService.addUser(toSave));
    }

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ReaderDTO updateReader(@PathVariable String id, @Valid @RequestBody ReaderDTO readerDTO) {
        Reader updates = UserConverter.fromReaderDTO(readerDTO);
        return UserConverter.toReaderDTO((Reader) userService.updateUser(id, updates));
    }

    @PostMapping("/{id}/activate")
    @ResponseStatus(HttpStatus.OK)
    public ReaderDTO activateReader(@PathVariable String id) {
        User user = userService.findUserById(id);
        if (!(user instanceof Reader)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User is not a Reader");
        }
        return UserConverter.toReaderDTO((Reader) userService.activateUser(id));
    }

    @PostMapping("/{id}/deactivate")
    @ResponseStatus(HttpStatus.OK)
    public ReaderDTO deactivateReader(@PathVariable String id) {
        User user = userService.findUserById(id);
        if (!(user instanceof Reader)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User is not a Reader");
        }
        return UserConverter.toReaderDTO((Reader) userService.deactivateUser(id));
    }
}