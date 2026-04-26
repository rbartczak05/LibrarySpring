package pl.lodz.p.library.adapters.rest.controllers;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
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

import java.time.LocalDateTime;
import java.util.Collections;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

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

    private Loan loan;
    private LoanDTO loanDTO;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
        LocalDateTime now = LocalDateTime.of(2023, 10, 27, 10, 0);
        loan = new Loan("reader1", "book1", now);
        loan.setId("loan1");

        loanDTO = new LoanDTO("loan1", now, now.plusDays(30), null, "book1", "reader1");
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void getAllLoans_ShouldReturnList() {
        when(loanUseCase.findAllLoans()).thenReturn(Collections.singletonList(loan));

        given()
        .when()
                .get("/loans")
        .then()
                .status(org.springframework.http.HttpStatus.OK)
                .body("_embedded.loans", hasSize(1));
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void getLoanById_ShouldReturnLoan() {
        when(loanUseCase.findLoanById("loan1")).thenReturn(loan);

        given()
        .when()
                .get("/loans/loan1")
        .then()
                .status(org.springframework.http.HttpStatus.OK)
                .body("id", equalTo("loan1"));
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void createLoan_ShouldCreateLoan() {
        when(loanUseCase.createLoan(anyString(), anyString(), any())).thenReturn(loan);

        given()
                .queryParam("readerId", "reader1")
                .queryParam("bookSetId", "book1")
        .when()
                .post("/loans")
        .then()
                .status(org.springframework.http.HttpStatus.CREATED)
                .body("id", equalTo("loan1"));
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void endLoan_ShouldEndLoan() {
        loan.setActive(false);
        when(loanUseCase.endLoan("loan1")).thenReturn(loan);

        given()
        .when()
                .post("/loans/loan1/end")
        .then()
                .status(org.springframework.http.HttpStatus.OK)
                .body("active", equalTo(false));
    }
}
