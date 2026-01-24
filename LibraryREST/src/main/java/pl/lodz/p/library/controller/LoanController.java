package pl.lodz.p.library.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.library.converter.LoanConverter;
import pl.lodz.p.library.dto.LoanDTO;
import pl.lodz.p.library.model.Loan;
import pl.lodz.p.library.model.User;
import pl.lodz.p.library.service.LoanService;
import pl.lodz.p.library.service.UserService;

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
    public List<LoanDTO> getAllLoans() {
        return loanService.findAllLoans().stream()
                .map(LoanConverter::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public LoanDTO getLoanById(@PathVariable String id) {
        return LoanConverter.toDTO((Loan) loanService.findLoanById(id));
    }

    @GetMapping("/reader_id/{readerId}")
    public List<LoanDTO> getLoansByReader(@PathVariable String readerId) {
        return loanService.findLoansByReader(readerId).stream().map(LoanConverter::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/bookset_id/{bookSetId}")
    public List<LoanDTO> getLoansByBookSet(@PathVariable String bookSetId) {
        return loanService.findLoansByBookSet(bookSetId).stream().map(LoanConverter::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/reader_bookset/{readerId}/{bookSetId}")
    public List<LoanDTO> getLoansByReaderAndBookSet(@PathVariable String readerId, @PathVariable String bookSetId) {
        return loanService.findLoansByReaderIdAndBookSetId(readerId, bookSetId).stream().map(LoanConverter::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/active/{active}")
    public List<LoanDTO> getActiveLoans(@PathVariable boolean active) {
        return loanService.findByActiveLoans(active).stream().map(LoanConverter::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/reader_id/{readerId}/active")
    public List<LoanDTO> getActiveLoansByReader(@PathVariable String readerId) {
        return loanService.findByReaderIdAndActive(readerId, true).stream().map(LoanConverter::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/reader_id/{readerId}/inactive")
    public List<LoanDTO> getInactiveLoansByReader(@PathVariable String readerId) {
        return loanService.findByReaderIdAndActive(readerId, false).stream().map(LoanConverter::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/bookset_id/{bookSetId}/active")
    public List<LoanDTO> getActiveLoansByBookSet(@PathVariable String bookSetId) {
        return loanService.findByBookSetIdAndActive(bookSetId, true).stream().map(LoanConverter::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/bookset_id/{bookSetId}/inactive")
    public List<LoanDTO> getInactiveLoansByBookSet(@PathVariable String bookSetId) {
        return loanService.findByBookSetIdAndActive(bookSetId, false).stream().map(LoanConverter::toDTO).collect(Collectors.toList());
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

    ///  Jakbysmy jednak sie zdecydowali na usuwanie wypożyczeń
//    @DeleteMapping("/{id}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void deleteLoan(@PathVariable String id) {
//        loanService.deleteLoan(id);
//    }
}
