package pl.lodz.p.library.web.controller.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.library.web.dto.LoanDTO;
import pl.lodz.p.library.web.service.RestLoanWebService;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanProxyApiController {

    private final RestLoanWebService restLoanWebService;

    public LoanProxyApiController(RestLoanWebService restLoanWebService) {
        this.restLoanWebService = restLoanWebService;
    }

    @GetMapping
    public List<LoanDTO> getAll() {
        return restLoanWebService.getAllLoans();
    }

    @GetMapping("/reader_id/{readerId}")
    public List<LoanDTO> getByReader(@PathVariable String readerId) {
        return restLoanWebService.getLoansByReader(readerId);
    }

    @GetMapping("/active/{active}")
    public List<LoanDTO> getByActive(@PathVariable boolean active) {
        return restLoanWebService.getLoansByActive(active);
    }

    @GetMapping("/reader_id/{readerId}/active/{active}")
    public List<LoanDTO> getByReaderAndActive(@PathVariable String readerId, @PathVariable boolean active) {
        return restLoanWebService.getLoansByReaderAndActive(readerId, active);
    }
}
