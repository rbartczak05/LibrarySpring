package pl.lodz.p.library.adapters.rest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import org.springframework.test.web.servlet.MockMvc;
import pl.lodz.p.library.adapters.rest.dto.LoanDTO;
import pl.lodz.p.library.adapters.rest.security.JwtService;
import pl.lodz.p.library.domain.model.Loan;
import pl.lodz.p.library.ports.inbound.LoanUseCase;
import pl.lodz.p.library.ports.inbound.UserUseCase;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoanController.class)
@AutoConfigureMockMvc(addFilters = false)
public class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LoanUseCase loanUseCase;

    @MockitoBean
    private UserUseCase userUseCase;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    private Loan loan;
    private LoanDTO loanDTO;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.of(2023, 10, 27, 10, 0);
        loan = new Loan("reader1", "book1", now);
        loan.setId("loan1");

        loanDTO = new LoanDTO("loan1", now, now.plusDays(30), null, "book1", "reader1");
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void getAllLoans_ShouldReturnList() throws Exception {
        when(loanUseCase.findAllLoans()).thenReturn(Collections.singletonList(loan));

        mockMvc.perform(get("/loans"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.loans.length()").value(1));
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void getLoanById_ShouldReturnLoan() throws Exception {
        when(loanUseCase.findLoanById("loan1")).thenReturn(loan);

        mockMvc.perform(get("/loans/loan1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("loan1"));
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void createLoan_ShouldCreateLoan() throws Exception {
        when(loanUseCase.createLoan(anyString(), anyString(), any())).thenReturn(loan);

        mockMvc.perform(post("/loans")
                        .param("readerId", "reader1")
                        .param("bookSetId", "book1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("loan1"));
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void endLoan_ShouldEndLoan() throws Exception {
        loan.setActive(false);
        when(loanUseCase.endLoan("loan1")).thenReturn(loan);

        mockMvc.perform(post("/loans/loan1/end"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(false));
    }
}
