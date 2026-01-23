package pl.lodz.p.library.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.lodz.p.library.converter.UserConverter;
import pl.lodz.p.library.dto.AdministratorDTO;
import pl.lodz.p.library.model.Administrator;
import pl.lodz.p.library.model.User;
import pl.lodz.p.library.security.JwtService;
import pl.lodz.p.library.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admins")
public class AdministratorController {

    private final UserService userService;
    private final JwtService jwtService;

    @Autowired
    public AdministratorController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @GetMapping
    public List<AdministratorDTO> getAllAdmins() {
        return userService.findAllUsers().stream()
                .filter(Administrator.class::isInstance)
                .map(Administrator.class::cast)
                .map(UserConverter::toAdministratorDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public AdministratorDTO getAdminById(@PathVariable String id) {
        User user = userService.findUserById(id);
        AdministratorDTO administratorDTO = UserConverter.toAdministratorDTO((Administrator) user);

        String signature = jwtService.generateSignatureForId(id);

        return ResponseEntity.ok()
                .header("If-Match", signature)
                .body(administratorDTO).getBody();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AdministratorDTO addAdmin(@Valid @RequestBody AdministratorDTO adminDTO) {
        Administrator toSave = UserConverter.fromAdministratorDTO(adminDTO);
        return UserConverter.toAdministratorDTO((Administrator) userService.addUser(toSave));
    }
}