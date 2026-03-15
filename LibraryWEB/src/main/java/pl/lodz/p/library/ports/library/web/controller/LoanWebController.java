package pl.lodz.p.library.ports.library.web.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.library.ports.library.web.dto.LoanDTO;
import pl.lodz.p.library.ports.library.web.service.RestLoanWebService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Controller
@RequestMapping("/loans")
public class LoanWebController {

    private final RestLoanWebService restLoanWebService;

    public LoanWebController(RestLoanWebService restLoanWebService) {
        this.restLoanWebService = restLoanWebService;
    }

    @GetMapping
    public String listLoans(Model model, @RequestParam(value = "success", required = false) String success,
                            @RequestParam(value = "error", required = false) String error) {
        model.addAttribute("loans", restLoanWebService.getAllLoans());
        model.addAttribute("success", success);
        model.addAttribute("error", error);
        return "loans/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("loanDto", new LoanDTO());
        return "loans/new";
    }

    @PostMapping
    public String createLoan(@Valid @ModelAttribute("loanDto") LoanDTO loanDto,
                             BindingResult bindingResult,
                             @RequestParam(value = "startTimeStr", required = false) String startTimeStr,
                             Model model) {
        if (bindingResult.hasErrors()) {
            return "loans/new";
        }
        LocalDateTime startTime = null;
        if (startTimeStr != null && !startTimeStr.isBlank()) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
                startTime = LocalDateTime.parse(startTimeStr, formatter);
            } catch (DateTimeParseException ex1) {
                try {
                    startTime = LocalDateTime.parse(startTimeStr);
                } catch (DateTimeParseException ex2) {
                    bindingResult.rejectValue("startTime", "invalid.format", "Niepoprawny format daty (wymagany ISO-8601, np. 2025-01-31T10:15)");
                    return "loans/new";
                }
            }
        }
        try {
            restLoanWebService.createLoan(loanDto.getReaderId(), loanDto.getBookSetId(), startTime);
        } catch (Exception ex) {
            model.addAttribute("globalError", ex.getMessage());
            return "loans/new";
        }
        return "redirect:/loans?success=created";
    }

    @PostMapping("/{id}/end")
    public String endLoan(@PathVariable("id") String id) {
        try {
            restLoanWebService.endLoan(id);
            return "redirect:/loans?success=ended";
        } catch (Exception ex) {
            return "redirect:/loans?error=" + ex.getMessage();
        }
    }
}
