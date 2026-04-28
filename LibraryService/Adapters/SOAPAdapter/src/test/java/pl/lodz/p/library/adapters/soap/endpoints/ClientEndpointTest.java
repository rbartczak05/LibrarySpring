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
import pl.lodz.p.library.adapters.soap.converters.ClientSoapConverter;
import pl.lodz.p.library.domain.model.Client;
import pl.lodz.p.library.ports.inbound.ClientUseCase;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.ws.test.server.RequestCreators.withPayload;
import static org.springframework.ws.test.server.ResponseMatchers.noFault;
import static org.springframework.ws.test.server.ResponseMatchers.xpath;

@WebServiceServerTest
@Import({ClientEndpoint.class, ClientSoapConverter.class})
class ClientEndpointTest {

    @Autowired
    private ApplicationContext context;

    @MockitoBean
    private ClientUseCase clientUseCase;

    private MockWebServiceClient mockClient;
    private static final String NS = "http://pl.lodz.p.library.adapters.soap.dto.client/";
    private static final Map<String, String> NS_MAP = Map.of("ns", NS);

    @BeforeEach
    void setUp() {
        mockClient = MockWebServiceClient.createClient(context);
    }

    private Client buildClient(String id, String firstName, String lastName) {
        Client client = new Client(firstName, lastName, "email@test.pl", 25);
        client.setId(id);
        return client;
    }

    @Test
    void getAllClients_shouldReturnList() {
        when(clientUseCase.findAllClients()).thenReturn(List.of(
                buildClient("c-1", "Jan", "Kowalski")
        ));

        mockClient.sendRequest(withPayload(new StringSource(
                        "<GetAllClientsRequest xmlns=\"" + NS + "\"/>")))
                .andExpect(noFault())
                .andExpect(xpath("//ns:client[1]/ns:id", NS_MAP).evaluatesTo("c-1"))
                .andExpect(xpath("//ns:client[1]/ns:firstName", NS_MAP).evaluatesTo("Jan"));
    }

    @Test
    void getClientById_shouldReturnClient() {
        when(clientUseCase.findClientById("c-1")).thenReturn(buildClient("c-1", "Jan", "Kowalski"));

        mockClient.sendRequest(withPayload(new StringSource(
                        "<GetClientByIdRequest xmlns=\"" + NS + "\"><id>c-1</id></GetClientByIdRequest>")))
                .andExpect(noFault())
                .andExpect(xpath("//ns:clientDTO/ns:firstName", NS_MAP).evaluatesTo("Jan"));
    }
}