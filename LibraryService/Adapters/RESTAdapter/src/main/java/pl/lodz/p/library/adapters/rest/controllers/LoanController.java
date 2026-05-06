package pl.lodz.p.library.adapters.rest.controllers;

import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.library.adapters.rest.converters.LoanConverter;
import pl.lodz.p.library.adapters.rest.dto.LoanDTO;
import pl.lodz.p.library.domain.model.Client;
import pl.lodz.p.library.domain.model.Loan;
import pl.lodz.p.library.ports.inbound.ClientUseCase;
import pl.lodz.p.library.ports.inbound.LoanUseCase;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/loans")
public class LoanController {
    private final LoanUseCase loanUseCase;
    private final ClientUseCase clientUseCase;

    public LoanController(LoanUseCase loanUseCase, ClientUseCase clientUseCase) {
        this.loanUseCase = loanUseCase;
        this.clientUseCase = clientUseCase;
    }

    private static LoanDTO applyLinks(LoanDTO dto) {
        dto.add(linkTo(methodOn(LoanController.class).getLoanById(dto.getId())).withSelfRel());
        if (dto.isActive()) {
            dto.add(linkTo(methodOn(LoanController.class).endLoan(dto.getId())).withRel("end"));
        }
        dto.add(linkTo(methodOn(ClientController.class).getClientById(dto.getClientId())).withRel("client"));
        dto.add(linkTo(methodOn(BookSetController.class).getBookSetByID(dto.getBookSetId())).withRel("bookset"));
        dto.add(linkTo(methodOn(LoanController.class).deleteLoan(dto.getId())).withRel("delete"));
        return dto;
    }

    @GetMapping
    public CollectionModel<LoanDTO> getAllLoans() {
        List<LoanDTO> loans = loanUseCase.findAllLoans().stream()
                .map(LoanConverter::toDTO)
                .map(LoanController::applyLinks)
                .collect(Collectors.toList());
        return CollectionModel.of(loans, linkTo(methodOn(LoanController.class).getAllLoans()).withSelfRel());
    }

    @GetMapping("/{id}")
    public LoanDTO getLoanById(@PathVariable UUID id) {
        return applyLinks(LoanConverter.toDTO(loanUseCase.findLoanById(id)));
    }

    @GetMapping("/client_id/{clientId}")
    public CollectionModel<LoanDTO> getLoansByClient(@PathVariable UUID clientId) {
        List<LoanDTO> loans = loanUseCase.findByClientId(clientId).stream()
                .map(LoanConverter::toDTO)
                .map(LoanController::applyLinks)
                .collect(Collectors.toList());
        return CollectionModel.of(loans, linkTo(methodOn(LoanController.class).getLoansByClient(clientId)).withSelfRel());
    }

    @GetMapping("/bookset_id/{bookSetId}")
    public CollectionModel<LoanDTO> getLoansByBookSet(@PathVariable UUID bookSetId) {
        List<LoanDTO> loans = loanUseCase.findByBookSetId(bookSetId).stream()
                .map(LoanConverter::toDTO)
                .map(LoanController::applyLinks)
                .collect(Collectors.toList());
        return CollectionModel.of(loans, linkTo(methodOn(LoanController.class).getLoansByBookSet(bookSetId)).withSelfRel());
    }

    @GetMapping("/client_bookset/{clientId}/{bookSetId}")
    public CollectionModel<LoanDTO> getLoansByClientAndBookSet(@PathVariable UUID clientId, @PathVariable UUID bookSetId) {
        List<LoanDTO> loans = loanUseCase.findByClientIdAndBookSetId(clientId, bookSetId).stream()
                .map(LoanConverter::toDTO)
                .map(LoanController::applyLinks)
                .collect(Collectors.toList());
        return CollectionModel.of(loans, linkTo(methodOn(LoanController.class).getLoansByClientAndBookSet(clientId, bookSetId)).withSelfRel());
    }

    @GetMapping("/active/{active}")
    public CollectionModel<LoanDTO> getActiveLoans(@PathVariable boolean active) {
        List<LoanDTO> loans = loanUseCase.findByActiveLoans(active).stream()
                .map(LoanConverter::toDTO)
                .map(LoanController::applyLinks)
                .collect(Collectors.toList());
        return CollectionModel.of(loans, linkTo(methodOn(LoanController.class).getActiveLoans(active)).withSelfRel());
    }

    @GetMapping("/client_id/{clientId}/active")
    public CollectionModel<LoanDTO> getActiveLoansByClient(@PathVariable UUID clientId) {
        List<LoanDTO> loans = loanUseCase.findByClientIdAndActive(clientId, true).stream()
                .map(LoanConverter::toDTO)
                .map(LoanController::applyLinks)
                .collect(Collectors.toList());
        return CollectionModel.of(loans, linkTo(methodOn(LoanController.class).getActiveLoansByClient(clientId)).withSelfRel());
    }

    @GetMapping("/client_id/{clientId}/inactive")
    public CollectionModel<LoanDTO> getInactiveLoansByClient(@PathVariable UUID clientId) {
        List<LoanDTO> loans = loanUseCase.findByClientIdAndActive(clientId, false).stream()
                .map(LoanConverter::toDTO)
                .map(LoanController::applyLinks)
                .collect(Collectors.toList());
        return CollectionModel.of(loans, linkTo(methodOn(LoanController.class).getInactiveLoansByClient(clientId)).withSelfRel());
    }

    @GetMapping("/bookset_id/{bookSetId}/active")
    public CollectionModel<LoanDTO> getActiveLoansByBookSet(@PathVariable UUID bookSetId) {
        List<LoanDTO> loans = loanUseCase.findByBookSetIdAndActive(bookSetId, true).stream()
                .map(LoanConverter::toDTO)
                .map(LoanController::applyLinks)
                .collect(Collectors.toList());
        return CollectionModel.of(loans, linkTo(methodOn(LoanController.class).getActiveLoansByBookSet(bookSetId)).withSelfRel());
    }

    @GetMapping("/bookset_id/{bookSetId}/inactive")
    public CollectionModel<LoanDTO> getInactiveLoansByBookSet(@PathVariable UUID bookSetId) {
        List<LoanDTO> loans = loanUseCase.findByBookSetIdAndActive(bookSetId, false).stream()
                .map(LoanConverter::toDTO)
                .map(LoanController::applyLinks)
                .collect(Collectors.toList());
        return CollectionModel.of(loans, linkTo(methodOn(LoanController.class).getInactiveLoansByBookSet(bookSetId)).withSelfRel());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LoanDTO createLoan(@RequestParam UUID clientId, @RequestParam UUID bookSetId, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime loanStartTime) {
        return LoanConverter.toDTO(loanUseCase.createLoan(clientId, bookSetId, loanStartTime));
    }

    @PostMapping("/me")
    @ResponseStatus(HttpStatus.CREATED)
    public LoanDTO createMyLoan(@RequestParam UUID bookSetId) {
        String currentClientId = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientUseCase.findClientById(UUID.fromString(currentClientId));
        return LoanConverter.toDTO(loanUseCase.createLoan(client.getId(), bookSetId, LocalDateTime.now()));
    }

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LoanDTO updateLoan(@PathVariable UUID id, @Valid @RequestBody LoanDTO loanDto) {
        Loan loanUpdates = LoanConverter.fromDTO(loanDto);
        return LoanConverter.toDTO(loanUseCase.updateLoan(id, loanUpdates));
    }

    @PostMapping("/{id}/end")
    @ResponseStatus(HttpStatus.OK)
    public LoanDTO endLoan(@PathVariable UUID id) {
        return LoanConverter.toDTO(loanUseCase.endLoan(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> deleteLoan(@PathVariable UUID id) {
        loanUseCase.deleteLoan(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public CollectionModel<LoanDTO> getMyLoans() {
        String currentClientId = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientUseCase.findClientById(UUID.fromString(currentClientId));
        List<LoanDTO> loans = loanUseCase.findByClientId(client.getId()).stream()
                .map(LoanConverter::toDTO)
                .map(LoanController::applyLinks)
                .collect(Collectors.toList());
        return CollectionModel.of(loans, linkTo(methodOn(LoanController.class).getMyLoans()).withSelfRel());
    }
}