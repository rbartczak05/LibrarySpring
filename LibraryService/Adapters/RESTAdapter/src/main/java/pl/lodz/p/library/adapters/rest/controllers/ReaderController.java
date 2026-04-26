package pl.lodz.p.library.adapters.rest.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.lodz.p.library.adapters.rest.converters.UserConverter;
import pl.lodz.p.library.adapters.rest.dto.ReaderDTO;
import pl.lodz.p.library.adapters.rest.security.JwtService;
import pl.lodz.p.library.domain.model.Reader;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/readers")
public class ReaderController {
    private final UserUseCase userUseCase;
    private final JwtService jwtService;

    public ReaderController(UserUseCase userUseCase, JwtService jwtService) {
        this.userUseCase = userUseCase;
        this.jwtService = jwtService;
    }

    @GetMapping
    public List<ReaderDTO> getAllReaders() {
        return userUseCase.findAllUsers().stream()
                .filter(Reader.class::isInstance)
                .map(Reader.class::cast)
                .map(UserConverter::toReaderDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReaderDTO> getReaderById(@PathVariable String id) {
        User user = userUseCase.findUserById(id);
        ReaderDTO readerDto = UserConverter.toReaderDTO((Reader) user);
        String signature = jwtService.generateSignatureForId(id);
        return ResponseEntity.ok()
                .header("If-Match", signature)
                .body(readerDto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReaderDTO addReader(@Valid @RequestBody ReaderDTO readerDTO) {
        Reader toSave = UserConverter.fromReaderDTO(readerDTO);
        return UserConverter.toReaderDTO((Reader) userUseCase.addUser(toSave));
    }

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ReaderDTO updateReader(@PathVariable String id, @RequestHeader(value = "If-Match", required = false) String ifMatch, @Valid @RequestBody ReaderDTO readerDTO) {
        if (ifMatch == null || ifMatch.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Niepoprawny nagłówek.");
        }
        String cleanIfMatch = ifMatch.replace("\"", "");
        if (!jwtService.verifySignature(id, cleanIfMatch)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Niepoprawne dane.");
        }
        Reader updates = UserConverter.fromReaderDTO(readerDTO);
        return UserConverter.toReaderDTO((Reader) userUseCase.updateUser(id, updates));
    }

    @PostMapping("/{id}/activate")
    @ResponseStatus(HttpStatus.OK)
    public ReaderDTO activateReader(@PathVariable String id) {
        User user = userUseCase.findUserById(id);
        if (!(user instanceof Reader)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Użytkownik nie jest czytelnikiem. Nie można aktywować.");
        }
        return UserConverter.toReaderDTO((Reader) userUseCase.activateUser(id));
    }

    @PostMapping("/{id}/deactivate")
    @ResponseStatus(HttpStatus.OK)
    public ReaderDTO deactivateReader(@PathVariable String id) {
        User user = userUseCase.findUserById(id);
        if (!(user instanceof Reader)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Użytkownik nie jest czytelnikiem. Nie można de aktywować.");
        }
        return UserConverter.toReaderDTO((Reader) userUseCase.deactivateUser(id));
    }
}