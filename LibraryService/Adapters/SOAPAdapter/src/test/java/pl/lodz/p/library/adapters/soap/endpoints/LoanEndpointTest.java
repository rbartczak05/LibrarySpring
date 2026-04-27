package pl.lodz.p.library.adapters.soap.endpoints;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.webservices.server.WebServiceServerTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.ws.test.server.MockWebServiceClient;
import org.springframework.xml.transform.StringSource;
import pl.lodz.p.library.adapters.soap.converters.LoanSoapConverter;
import pl.lodz.p.library.domain.model.Loan;
import pl.lodz.p.library.ports.inbound.LoanUseCase;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.ws.test.server.RequestCreators.withPayload;
import static org.springframework.ws.test.server.ResponseMatchers.noFault;
import static org.springframework.ws.test.server.ResponseMatchers.xpath;

@WebServiceServerTest
@Import({LoanEndpoint.class, LoanSoapConverter.class})
class LoanEndpointTest {

    @Autowired
    private ApplicationContext context;

    @MockitoBean
    private LoanUseCase loanUseCase;

    private MockWebServiceClient client;
    private static final String NS = "http://pl.lodz.p.library.adapters.soap.dto.loan/";
    private static final Map<String, String> NS_MAP = Map.of("ns", NS);

    @BeforeEach
    void setUp() {
        client = MockWebServiceClient.createClient(context);
    }

    private Loan buildLoan(String id, String clientId, String bookSetId) {
        Loan loan = new Loan(clientId, bookSetId, LocalDateTime.parse("2023-01-01T10:00:00"));
        loan.setId(id);
        return loan;
    }

    @Test
    void getAllLoans_shouldReturnList() {
        when(loanUseCase.findAllLoans()).thenReturn(List.of(
                buildLoan("l-1", "c-1", "b-1")
        ));

        client.sendRequest(withPayload(new StringSource(
                        "<GetAllLoansRequest xmlns=\"" + NS + "\"/>")))
                .andExpect(noFault())
                .andExpect(xpath("//ns:loans[1]/ns:id", NS_MAP).evaluatesTo("l-1"));
    }

    @Test
    void getLoanById_shouldReturnLoan() {
        when(loanUseCase.findLoanById("l-1")).thenReturn(buildLoan("l-1", "c-1", "b-1"));

        client.sendRequest(withPayload(new StringSource(
                        "<GetLoanByIdRequest xmlns=\"" + NS + "\"><id>l-1</id></GetLoanByIdRequest>")))
                .andExpect(noFault())
                .andExpect(xpath("//ns:loanDTO/ns:clientId", NS_MAP).evaluatesTo("c-1"));
    }

    @Test
    void getLoansByClient_shouldReturnList() {
        when(loanUseCase.findByClientId("c-1")).thenReturn(List.of(
                buildLoan("l-1", "c-1", "b-1")
        ));

        client.sendRequest(withPayload(new StringSource(
                        "<GetLoansByClientRequest xmlns=\"" + NS + "\"><clientId>c-1</clientId></GetLoansByClientRequest>")))
                .andExpect(noFault())
                .andExpect(xpath("//ns:loanDTO[1]/ns:id", NS_MAP).evaluatesTo("l-1"));
    }

    @Test
    void createLoan_shouldReturnCreatedLoan() {
        when(loanUseCase.createLoan("c-1", "b-1")).thenReturn(buildLoan("l-new", "c-1", "b-1"));

        client.sendRequest(withPayload(new StringSource("""
                <CreateLoanRequest xmlns="%s">
                    <clientId>c-1</clientId>
                    <bookSetId>b-1</bookSetId>
                </CreateLoanRequest>""".formatted(NS))))
                .andExpect(noFault())
                .andExpect(xpath("//ns:loanDTO/ns:id", NS_MAP).evaluatesTo("l-new"))
                .andExpect(xpath("//ns:loanDTO/ns:active", NS_MAP).evaluatesTo("true"));
    }

    @Test
    void endLoan_shouldReturnInactiveLoan() {
        Loan ended = buildLoan("l-end", "c-5", "b-5");
        ended.setActive(false);
        when(loanUseCase.endLoan("l-end")).thenReturn(ended);

        client.sendRequest(withPayload(new StringSource(
                        "<EndLoanRequest xmlns=\"" + NS + "\"><id>l-end</id></EndLoanRequest>")))
                .andExpect(noFault())
                .andExpect(xpath("//ns:loanDTO/ns:id", NS_MAP).evaluatesTo("l-end"))
                .andExpect(xpath("//ns:loanDTO/ns:active", NS_MAP).evaluatesTo("false"));
    }

    @Test
    void deleteLoan_shouldReturnDeleted() {
        client.sendRequest(withPayload(new StringSource(
                        "<DeleteLoanRequest xmlns=\"" + NS + "\"><id>l-del</id></DeleteLoanRequest>")))
                .andExpect(noFault())
                .andExpect(xpath("//ns:deleted", NS_MAP).evaluatesTo("true"));

        verify(loanUseCase).deleteLoan("l-del");
    }
}