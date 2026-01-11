package pl.lodz.p.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.lodz.p.library.converter.LoanConverter;
import pl.lodz.p.library.dto.LoanDTO;
import pl.lodz.p.library.exception.BookSetNotAvailableException;
import pl.lodz.p.library.exception.LoanNotFoundException;
import pl.lodz.p.library.exception.ReaderLimitsException;
import pl.lodz.p.library.model.Loan;
import pl.lodz.p.library.service.LoanService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoanController.class)
class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LoanService loanService;

    @Autowired
    private ObjectMapper objectMapper;

    private Loan loan;
    private LoanDTO loanDTO;
    private String testId;
    private String readerId;
    private String bookSetId;

    @BeforeEach
    void setUp() {
        readerId = UUID.randomUUID().toString();
        bookSetId = UUID.randomUUID().toString();
        loan = new Loan(readerId, bookSetId, LocalDateTime.now());
        loan.setId(UUID.randomUUID().toString());

        testId = loan.getId();

        loanDTO = LoanConverter.toDTO(loan);
    }

    @Test
    void getAllLoansTest() throws Exception {
        when(loanService.findAllLoans()).thenReturn(List.of(loan));

        mockMvc.perform(get("/loans"))
                .andExpect(status().isOk())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(testId)));
    }

    @Test
    void getLoanByIdTest() throws Exception {
        when(loanService.findLoanById(testId)).thenReturn(loan);

        mockMvc.perform(get("/loans/{id}", testId))
                .andExpect(status().isOk())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id", is(testId)));
    }

    @Test
    void getLoanByIdNotFoundTest() throws Exception {
        when(loanService.findLoanById(any(String.class)))
                .thenThrow(new LoanNotFoundException(HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/loans/{id}", testId))
                .andExpect(status().isNotFound())
                .andExpect(status().is(404));
    }

    @Test
    void getLoansByReaderTest() throws Exception {
        when(loanService.findLoansByReader(readerId)).thenReturn(List.of(loan));

        mockMvc.perform(get("/loans/reader_id/{readerId}", readerId))
                .andExpect(status().isOk())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getLoansByBookSetTest() throws Exception {
        when(loanService.findLoansByBookSet(bookSetId)).thenReturn(List.of(loan));

        mockMvc.perform(get("/loans/bookset_id/{bookSetId}", bookSetId))
                .andExpect(status().isOk())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getLoansByReaderAndBookSetTest() throws Exception {
        when(loanService.findLoansByReaderIdAndBookSetId(readerId, bookSetId)).thenReturn(List.of(loan));

        mockMvc.perform(get("/loans/reader_bookset/{readerId}/{bookSetId}", readerId, bookSetId))
                .andExpect(status().isOk())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getActiveLoansTest() throws Exception {
        when(loanService.findByActiveLoans(true)).thenReturn(List.of(loan));

        mockMvc.perform(get("/loans/active/{active}", true))
                .andExpect(status().isOk())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getActiveLoansByReaderTest() throws Exception {
        when(loanService.findByReaderIdAndActive(readerId, true)).thenReturn(List.of(loan));

        mockMvc.perform(get("/loans/reader_id/{readerId}/active", readerId))
                .andExpect(status().isOk())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getInactiveLoansByBookSetTest() throws Exception {
        when(loanService.findByBookSetIdAndActive(bookSetId, false)).thenReturn(List.of(loan));

        mockMvc.perform(get("/loans/bookset_id/{bookSetId}/inactive", bookSetId))
                .andExpect(status().isOk())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void createLoanTest() throws Exception {
        when(loanService.createLoan(eq(readerId), eq(bookSetId), any())).thenReturn(loan);

        mockMvc.perform(post("/loans")
                        .param("readerId", readerId)
                        .param("bookSetId", bookSetId))
                .andExpect(status().isCreated())
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.id", is(testId)));
    }

    @Test
    void createLoanWithStartTimeTest() throws Exception {
        LocalDateTime startTime = LocalDateTime.of(2025, 1, 1, 10, 0, 0);
        loan.setStartTime(startTime);
        when(loanService.createLoan(readerId, bookSetId, startTime)).thenReturn(loan);

        mockMvc.perform(post("/loans")
                        .param("readerId", readerId)
                        .param("bookSetId", bookSetId)
                        .param("loanStartTime", "2025-01-01T10:00:00"))
                .andExpect(status().isCreated())
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.startTime", is("2025-01-01T10:00:00")));
    }

    @Test
    void createLoanBadRequestTest() throws Exception {
        when(loanService.createLoan(any(), any(), any()))
                .thenThrow(new ReaderLimitsException(HttpStatus.BAD_REQUEST));

        mockMvc.perform(post("/loans")
                        .param("readerId", readerId)
                        .param("bookSetId", bookSetId))
                .andExpect(status().isBadRequest())
                .andExpect(status().is(400));
    }

    @Test
    void createLoanFailWhenResourceAlreadyAllocatedTest() throws Exception {
        when(loanService.createLoan(any(), any(), any()))
                .thenThrow(new BookSetNotAvailableException(HttpStatus.CONFLICT, "BookSet is not available"));

        mockMvc.perform(post("/loans")
                        .param("readerId", readerId)
                        .param("bookSetId", bookSetId))
                .andExpect(status().isConflict())
                .andExpect(status().reason("BookSet is not available"));
    }

    @Test
    void createLoanFailReaderLimitTest() throws Exception {
        when(loanService.createLoan(any(), any(), any()))
                .thenThrow(new ReaderLimitsException(HttpStatus.CONFLICT, "Reader reached the maximum number of loans"));

        mockMvc.perform(post("/loans")
                        .param("readerId", readerId)
                        .param("bookSetId", bookSetId))
                .andExpect(status().isConflict())
                .andExpect(status().reason("Reader reached the maximum number of loans"));
    }

    @Test
    void updateLoanTest() throws Exception {
        when(loanService.updateLoan(any(String.class), any(Loan.class))).thenReturn(loan);

        mockMvc.perform(post("/loans/{id}", testId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loanDTO)))
                .andExpect(status().isOk())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id", is(testId)));
    }

    @Test
    void endLoanTest() throws Exception {
        loan.setActive(false);
        when(loanService.endLoan(testId)).thenReturn(loan);

        mockMvc.perform(post("/loans/{id}/end", testId))
                .andExpect(status().isOk())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.active", is(false)));
    }

    @Test
    void endLoanNotFoundTest() throws Exception {
        when(loanService.endLoan(testId))
                .thenThrow(new LoanNotFoundException(HttpStatus.NOT_FOUND));

        mockMvc.perform(post("/loans/{id}/end", testId))
                .andExpect(status().isNotFound())
                .andExpect(status().is(404));
    }

    
}