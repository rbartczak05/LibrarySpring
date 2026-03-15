package pl.lodz.p.library.ports.library.web.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;
import pl.lodz.p.library.ports.library.web.dto.ReaderDTO;
import pl.lodz.p.library.ports.library.web.service.RestReaderWebService;

@Controller
@RequestMapping("/readers")
public class ReaderWebController {

    private final RestReaderWebService restReaderWebService;

    public ReaderWebController(RestReaderWebService restReaderWebService) {
        this.restReaderWebService = restReaderWebService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("readerDto", new ReaderDTO());
        return "register";
    }

    @PostMapping("/register")
    public String registerReader(@Valid @ModelAttribute("readerDto") ReaderDTO readerDto,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        try {
            restReaderWebService.registerReader(readerDto);
        } catch (HttpClientErrorException e) {
            bindingResult.reject("error.userExists");
            return "register";
        }
        return "redirect:/readers/register?success";
    }

}