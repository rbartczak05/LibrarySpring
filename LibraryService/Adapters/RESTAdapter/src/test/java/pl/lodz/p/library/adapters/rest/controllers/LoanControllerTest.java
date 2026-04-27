package pl.lodz.p.library.adapters.rest.controllers;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.lodz.p.library.adapters.rest.dto.LoanDTO;
import pl.lodz.p.library.adapters.rest.security.JwtService;
import pl.lodz.p.library.domain.model.Loan;
import pl.lodz.p.library.domain.model.Client;
import pl.lodz.p.library.ports.inbound.LoanUseCase;
import pl.lodz.p.library.ports.inbound.ClientUseCase;

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
    private ClientUseCase clientUseCase;

    @MockitoBean
    private JwtService jwtService;

    private Loan loan;
    private LoanDTO loanDTO;
    private Client client;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);

        client = new Client("Jan", "Kowalski", "jan@test.pl", 25);
        client.setId("client1");

        loan = new Loan("client1", "book1", LocalDateTime.now());
        loan.setId("loan1");

        loanDTO = new LoanDTO();
        loanDTO.setId("loan1");
        loanDTO.setClientId("client1");
        loanDTO.setBookSetId("book1");
        loanDTO.setActive(true);
        loanDTO.setStartTime(loan.getStartTime());
        loanDTO.setEndTime(loan.getEndTime());
    }

    @Test
    @WithMockUser
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
    @WithMockUser
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
    @WithMockUser
    void createLoan_ShouldCreateLoan() {
        when(loanUseCase.createLoan(anyString(), anyString(), any())).thenReturn(loan);

        given()
                .queryParam("clientId", "client1")
                .queryParam("bookSetId", "book1")
                .when()
                .post("/loans")
                .then()
                .status(org.springframework.http.HttpStatus.CREATED)
                .body("id", equalTo("loan1"));
    }

    @Test
    @WithMockUser
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