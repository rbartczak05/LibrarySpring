package pl.lodz.p.library.adapters.rest.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.library.converter.LoanConverter;
import pl.lodz.p.library.dto.LoanDTO;
import pl.lodz.p.library.model.Loan;
import pl.lodz.p.library.model.User;
import pl.lodz.p.library.service.LoanService;
import pl.lodz.p.library.service.UserService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/loans")
public class LoanController {

    private final LoanService loanService;
    private final UserService userService;

    @Autowired
    public LoanController(LoanService loanService, UserService userService) {
        this.loanService = loanService;
        this.userService = userService;
    }

    @GetMapping
    public CollectionModel<LoanDTO> getAllLoans() {
        List<LoanDTO> loans = loanService.findAllLoans().stream()
                .map(LoanConverter::toDTO)
                .map(LoanController::applyLinks)
                .collect(Collectors.toList());

        return CollectionModel.of(loans,
                linkTo(methodOn(LoanController.class).getAllLoans()).withSelfRel());
    }

    @GetMapping("/{id}")
    public LoanDTO getLoanById(@PathVariable String id) {
        return applyLinks(LoanConverter.toDTO(loanService.findLoanById(id)));
    }

    @GetMapping("/reader_id/{readerId}")
    public CollectionModel<LoanDTO> getLoansByReader(@PathVariable String readerId) {
        List<LoanDTO> loans = loanService
                .findLoansByReader(readerId).stream()
                .map(LoanConverter::toDTO)
                .map(LoanController::applyLinks)
                .collect(Collectors.toList());

        return CollectionModel.of(loans,
                linkTo(methodOn(LoanController.class).getLoansByReader(readerId)).withSelfRel());
    }

    @GetMapping("/bookset_id/{bookSetId}")
    public CollectionModel<LoanDTO> getLoansByBookSet(@PathVariable String bookSetId) {
        List<LoanDTO> loans = loanService.findLoansByBookSet(bookSetId).stream()
                .map(LoanConverter::toDTO)
                .map(LoanController::applyLinks)
                .collect(Collectors.toList());

        return CollectionModel.of(loans,
                linkTo(methodOn(LoanController.class).getLoansByBookSet(bookSetId)).withSelfRel());
    }

    @GetMapping("/reader_bookset/{readerId}/{bookSetId}")
    public CollectionModel<LoanDTO> getLoansByReaderAndBookSet(@PathVariable String readerId, @PathVariable String bookSetId) {
        List<LoanDTO> loans = loanService.findLoansByReaderIdAndBookSetId(readerId, bookSetId).stream()
                .map(LoanConverter::toDTO)
                .map(LoanController::applyLinks)
                .collect(Collectors.toList());

        return CollectionModel.of(loans,
                linkTo(methodOn(LoanController.class).getLoansByReaderAndBookSet(readerId, bookSetId)).withSelfRel());
    }

    @GetMapping("/active/{active}")
    public CollectionModel<LoanDTO> getActiveLoans(@PathVariable boolean active) {
        List<LoanDTO> loans = loanService.findByActiveLoans(active).stream()
                .map(LoanConverter::toDTO)
                .map(LoanController::applyLinks)
                .collect(Collectors.toList());

        return CollectionModel.of(loans,
                linkTo(methodOn(LoanController.class).getActiveLoans(active)).withSelfRel());
    }

    @GetMapping("/reader_id/{readerId}/active")
    public CollectionModel<LoanDTO> getActiveLoansByReader(@PathVariable String readerId) {
        List<LoanDTO> loans = loanService.findByReaderIdAndActive(readerId, true).stream()
                .map(LoanConverter::toDTO)
                .map(LoanController::applyLinks)
                .collect(Collectors.toList());

        return CollectionModel.of(loans,
                linkTo(methodOn(LoanController.class).getActiveLoansByReader(readerId)).withSelfRel());
    }

    @GetMapping("/reader_id/{readerId}/inactive")
    public CollectionModel<LoanDTO> getInactiveLoansByReader(@PathVariable String readerId) {
        List<LoanDTO> loans = loanService.findByReaderIdAndActive(readerId, false).stream()
                .map(LoanConverter::toDTO)
                .map(LoanController::applyLinks)
                .collect(Collectors.toList());

        return CollectionModel.of(loans,
                linkTo(methodOn(LoanController.class).getInactiveLoansByReader(readerId)).withSelfRel());
    }

    @GetMapping("/bookset_id/{bookSetId}/active")
    public CollectionModel<LoanDTO> getActiveLoansByBookSet(@PathVariable String bookSetId) {
    List<LoanDTO> loans = loanService.findByBookSetIdAndActive(bookSetId, true).stream()
            .map(LoanConverter::toDTO)
            .map(LoanController::applyLinks)
            .collect(Collectors.toList());

    return CollectionModel.of(loans,
            linkTo(methodOn(LoanController.class).getActiveLoansByBookSet(bookSetId)).withSelfRel());
    }

    @GetMapping("/bookset_id/{bookSetId}/inactive")
    public CollectionModel<LoanDTO> getInactiveLoansByBookSet(@PathVariable String bookSetId) {
        List<LoanDTO> loans = loanService.findByBookSetIdAndActive(bookSetId, false).stream()
                .map(LoanConverter::toDTO)
                .map(LoanController::applyLinks)
                .collect(Collectors.toList());

        return CollectionModel.of(loans,
                linkTo(methodOn(LoanController.class).getInactiveLoansByBookSet(bookSetId)).withSelfRel());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LoanDTO createLoan(@RequestParam String readerId, @RequestParam String bookSetId,
                              @RequestParam(required = false)
                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime loanStartTime) {
        return LoanConverter.toDTO(loanService.createLoan(readerId, bookSetId, loanStartTime));
    }

    @PostMapping("/me")
    @ResponseStatus(HttpStatus.CREATED)
    public LoanDTO createMyLoan(@RequestParam String bookSetId) {
        String currentLogin = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findUserByLogin(currentLogin);

        return LoanConverter.toDTO(loanService.createLoan(user.getId(), bookSetId, LocalDateTime.now()));
    }

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LoanDTO updateLoan(@PathVariable String id, @Valid @RequestBody LoanDTO loanDto) {
        Loan loanUpdates = LoanConverter.fromDTO(loanDto);
        return LoanConverter.toDTO(loanService.updateLoan(id, loanUpdates));
    }

    @PostMapping("/{id}/end")
    @ResponseStatus(HttpStatus.OK)
    public LoanDTO endLoan(@PathVariable String id) {
        return LoanConverter.toDTO(loanService.endLoan(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> deleteLoan(@PathVariable String id) {
        loanService.deleteLoan(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public CollectionModel<LoanDTO> getMyLoans() {
        String currentLogin = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findUserByLogin(currentLogin);
        List<LoanDTO> loans = loanService.findLoansByReader(user.getId()).stream()
                .map(LoanConverter::toDTO)
                .map(LoanController::applyLinks)
                .collect(Collectors.toList());

        return CollectionModel.of(loans,
                linkTo(methodOn(LoanController.class).getMyLoans()).withSelfRel());
    }

    //Funkcja pomocnicza do mapowania w strumieniach
    private static LoanDTO applyLinks(LoanDTO dto) {
        dto.add(linkTo(methodOn(LoanController.class).getLoanById(dto.getId())).withSelfRel());

        if (dto.isActive()) {
            dto.add(linkTo(methodOn(LoanController.class).endLoan(dto.getId())).withRel("end"));
        }

        dto.add(linkTo(methodOn(ReaderController.class).getReaderById(dto.getReaderId())).withRel("reader"));
        dto.add(linkTo(methodOn(BookSetController.class).getBookSetByID(dto.getBookSetId())).withRel("bookset"));

        dto.add(linkTo(methodOn(LoanController.class).deleteLoan(dto.getId())).withRel("delete"));

        return dto;
    }
}
