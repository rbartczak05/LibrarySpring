package pl.lodz.p.library.adapters.rest.controllers;

import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.library.adapters.rest.converters.ClientConverter;
import pl.lodz.p.library.adapters.rest.dto.ClientDTO;
import pl.lodz.p.library.domain.model.Client;
import pl.lodz.p.library.ports.inbound.ClientUseCase;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientUseCase clientUseCase;

    public ClientController(ClientUseCase clientUseCase) {
        this.clientUseCase = clientUseCase;
    }

    @GetMapping
    public CollectionModel<ClientDTO> getAllClients() {
        List<ClientDTO> clients = clientUseCase.findAllClients().stream()
                .map(ClientConverter::toDTO)
                .map(ClientController::applyLinks)
                .collect(Collectors.toList());
        return CollectionModel.of(clients, linkTo(methodOn(ClientController.class).getAllClients()).withSelfRel());
    }

    @GetMapping("/{id}")
    public ClientDTO getClientById(@PathVariable String id) {
        return applyLinks(ClientConverter.toDTO(clientUseCase.findClientById(id)));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClientDTO addClient(@Valid @RequestBody ClientDTO clientDTO) {
        Client client = ClientConverter.fromDTO(clientDTO);
        return ClientConverter.toDTO(clientUseCase.addClient(client));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> deleteClient(@PathVariable String id) {
        clientUseCase.deleteClient(id);
        return ResponseEntity.ok().build();
    }

    private static ClientDTO applyLinks(ClientDTO dto) {
        dto.add(linkTo(methodOn(ClientController.class).getClientById(dto.getId())).withSelfRel());
        dto.add(linkTo(methodOn(ClientController.class).deleteClient(dto.getId())).withRel("delete"));
        return dto;
    }
}