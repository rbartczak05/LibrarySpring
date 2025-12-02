package pl.lodz.p.library.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.lodz.p.library.converter.UserConverter;
import pl.lodz.p.library.dto.AdministratorDTO;
import pl.lodz.p.library.model.Administrator;
import pl.lodz.p.library.model.User;
import pl.lodz.p.library.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admins")
public class AdministratorController {

    private final UserService userService;

    @Autowired
    public AdministratorController(UserService userService) {
        this.userService = userService;
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
        if (!(user instanceof Administrator)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User is not an Administrator");
        }
        return UserConverter.toAdministratorDTO((Administrator) user);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AdministratorDTO addAdmin(@Valid @RequestBody AdministratorDTO adminDTO) {
        Administrator toSave = UserConverter.fromAdministratorDTO(adminDTO);
        return UserConverter.toAdministratorDTO((Administrator) userService.addUser(toSave));
    }
}