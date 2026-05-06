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
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.ws.test.server.RequestCreators.withPayload;
import static org.springframework.ws.test.server.ResponseMatchers.noFault;
import static org.springframework.ws.test.server.ResponseMatchers.xpath;

@WebServiceServerTest
@Import({LoanEndpoint.class, LoanSoapConverter.class})
class LoanEndpointTest {

    private static final String NS = "http://pl.lodz.p.library.adapters.soap.dto.loan/";
    private static final Map<String, String> NS_MAP = Map.of("ns", NS);
    @Autowired
    private ApplicationContext context;
    @MockitoBean
    private LoanUseCase loanUseCase;
    private MockWebServiceClient client;
    private UUID loan1Id;
    private UUID loanNewId;
    private UUID loanEndId;
    private UUID loanDelId;
    private UUID client1Id;
    private UUID client5Id;
    private UUID book1Id;
    private UUID book5Id;

    @BeforeEach
    void setUp() {
        client = MockWebServiceClient.createClient(context);
        loan1Id = UUID.randomUUID();
        loanNewId = UUID.randomUUID();
        loanEndId = UUID.randomUUID();
        loanDelId = UUID.randomUUID();
        client1Id = UUID.randomUUID();
        client5Id = UUID.randomUUID();
        book1Id = UUID.randomUUID();
        book5Id = UUID.randomUUID();
    }

    private Loan buildLoan(UUID id, UUID clientId, UUID bookSetId) {
        Loan loan = new Loan(clientId, bookSetId, LocalDateTime.parse("2023-01-01T10:00:01"));
        loan.setId(id);
        return loan;
    }

    @Test
    void getAllLoans_shouldReturnList() {
        when(loanUseCase.findAllLoans()).thenReturn(List.of(
                buildLoan(loan1Id, client1Id, book1Id)
        ));

        client.sendRequest(withPayload(new StringSource(
                        "<GetAllLoansRequest xmlns=\"" + NS + "\"/>")))
                .andExpect(noFault())
                .andExpect(xpath("//ns:loan[1]/ns:id", NS_MAP).evaluatesTo(loan1Id.toString()));
    }

    @Test
    void getLoanById_shouldReturnLoan() {
        when(loanUseCase.findLoanById(loan1Id)).thenReturn(buildLoan(loan1Id, client1Id, book1Id));

        client.sendRequest(withPayload(new StringSource(
                        "<GetLoanByIdRequest xmlns=\"" + NS + "\"><id>" + loan1Id + "</id></GetLoanByIdRequest>")))
                .andExpect(noFault())
                .andExpect(xpath("//ns:loan/ns:clientId", NS_MAP).evaluatesTo(client1Id.toString()));
    }

    @Test
    void getLoansByClient_shouldReturnList() {
        when(loanUseCase.findByClientId(client1Id)).thenReturn(List.of(
                buildLoan(loan1Id, client1Id, book1Id)
        ));

        client.sendRequest(withPayload(new StringSource(
                        "<GetLoansByClientRequest xmlns=\"" + NS + "\"><clientId>" + client1Id + "</clientId></GetLoansByClientRequest>")))
                .andExpect(noFault())
                .andExpect(xpath("//ns:loan[1]/ns:id", NS_MAP).evaluatesTo(loan1Id.toString()));
    }

    @Test
    void createLoan_shouldReturnCreatedLoan() {
        when(loanUseCase.createLoan(client1Id, book1Id)).thenReturn(buildLoan(loanNewId, client1Id, book1Id));

        client.sendRequest(withPayload(new StringSource("""
                        <CreateLoanRequest xmlns="%s">
                            <clientId>%s</clientId>
                            <bookSetId>%s</bookSetId>
                        </CreateLoanRequest>""".formatted(NS, client1Id.toString(), book1Id.toString()))))
                .andExpect(noFault())
                .andExpect(xpath("//ns:loan/ns:id", NS_MAP).evaluatesTo(loanNewId.toString()))
                .andExpect(xpath("//ns:loan/ns:active", NS_MAP).evaluatesTo("true"));
    }

    @Test
    void endLoan_shouldReturnInactiveLoan() {
        Loan ended = buildLoan(loanEndId, client5Id, book5Id);
        ended.setActive(false);
        when(loanUseCase.endLoan(loanEndId)).thenReturn(ended);

        client.sendRequest(withPayload(new StringSource(
                        "<EndLoanRequest xmlns=\"" + NS + "\"><id>" + loanEndId + "</id></EndLoanRequest>")))
                .andExpect(noFault())
                .andExpect(xpath("//ns:loan/ns:id", NS_MAP).evaluatesTo(loanEndId.toString()))
                .andExpect(xpath("//ns:loan/ns:active", NS_MAP).evaluatesTo("false"));
    }

    @Test
    void deleteLoan_shouldReturnDeleted() {
        client.sendRequest(withPayload(new StringSource(
                        "<DeleteLoanRequest xmlns=\"" + NS + "\"><id>" + loanDelId + "</id></DeleteLoanRequest>")))
                .andExpect(noFault())
                .andExpect(xpath("//ns:isDeleted", NS_MAP).evaluatesTo("true"));

        verify(loanUseCase).deleteLoan(loanDelId);
    }
}