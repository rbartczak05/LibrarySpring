package pl.lodz.p.library.adapters.rest.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.lodz.p.library.converter.UserConverter;
import pl.lodz.p.library.dto.ReaderDTO;
import pl.lodz.p.library.model.Reader;
import pl.lodz.p.library.model.User;
import pl.lodz.p.library.security.JwtService;
import pl.lodz.p.library.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/readers")
public class ReaderController {

    private final UserService userService;
    private final JwtService jwtService;

    @Autowired
    public ReaderController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
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

        ReaderDTO readerDto = UserConverter.toReaderDTO((Reader) user);

        String signature = jwtService.generateSignatureForId(id);

        return ResponseEntity.ok()
                .header("If-Match", signature)
                .body(readerDto).getBody();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReaderDTO addReader(@Valid @RequestBody ReaderDTO readerDTO) {
        Reader toSave = UserConverter.fromReaderDTO(readerDTO);
        return UserConverter.toReaderDTO((Reader) userService.addUser(toSave));
    }

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ReaderDTO updateReader(@PathVariable String id,
                                  @RequestHeader(value = "If-Match", required = false) String ifMatch,
                                  @Valid @RequestBody ReaderDTO readerDTO) {

        if (ifMatch == null || ifMatch.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Niepoprawny nagłówek.");
        }

        if (!jwtService.verifySignature(id, ifMatch)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Niepoprawne dane.");
        }

        Reader updates = UserConverter.fromReaderDTO(readerDTO);

        return UserConverter.toReaderDTO((Reader) userService.updateUser(id, updates));
    }

    @PostMapping("/{id}/activate")
    @ResponseStatus(HttpStatus.OK)
    public ReaderDTO activateReader(@PathVariable String id) {
        User user = userService.findUserById(id);
        if (!(user instanceof Reader)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Użytkownik nie jest czytelnikiem. Nie można aktywować.");
        }
        return UserConverter.toReaderDTO((Reader) userService.activateUser(id));
    }

    @PostMapping("/{id}/deactivate")
    @ResponseStatus(HttpStatus.OK)
    public ReaderDTO deactivateReader(@PathVariable String id) {
        User user = userService.findUserById(id);
        if (!(user instanceof Reader)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Użytkownik nie jest czytelnikiem. Nie można de aktywować.");
        }
        return UserConverter.toReaderDTO((Reader) userService.deactivateUser(id));
    }
}