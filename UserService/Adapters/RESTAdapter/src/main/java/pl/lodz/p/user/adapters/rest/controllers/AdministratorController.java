package pl.lodz.p.user.adapters.rest.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.user.adapters.rest.converters.UserConverter;
import pl.lodz.p.user.adapters.rest.dto.AdministratorDTO;
import pl.lodz.p.user.adapters.rest.security.JwtService;
import pl.lodz.p.user.domain.model.Administrator;
import pl.lodz.p.user.domain.model.User;
import pl.lodz.p.user.ports.inbound.UserUseCase;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admins")
public class AdministratorController {
    private final UserUseCase userUseCase;
    private final JwtService jwtService;

    public AdministratorController(UserUseCase userUseCase, JwtService jwtService) {
        this.userUseCase = userUseCase;
        this.jwtService = jwtService;
    }

    @GetMapping
    public List<AdministratorDTO> getAllAdmins() {
        return userUseCase.findAllUsers().stream()
                .filter(Administrator.class::isInstance)
                .map(Administrator.class::cast)
                .map(UserConverter::toAdministratorDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdministratorDTO> getAdminById(@PathVariable String id) {
        User user = userUseCase.findUserById(id);
        AdministratorDTO administratorDTO = UserConverter.toAdministratorDTO((Administrator) user);
        String signature = jwtService.generateSignatureForId(id);
        return ResponseEntity.ok()
                .header("If-Match", signature)
                .body(administratorDTO);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AdministratorDTO addAdmin(@Valid @RequestBody AdministratorDTO adminDTO) {
        Administrator toSave = UserConverter.fromAdministratorDTO(adminDTO);
        return UserConverter.toAdministratorDTO((Administrator) userUseCase.addUser(toSave));
    }
}