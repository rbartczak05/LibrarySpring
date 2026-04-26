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
import pl.lodz.p.library.adapters.soap.converters.UserSoapConverter;
import pl.lodz.p.library.domain.model.Reader;
import pl.lodz.p.library.domain.model.User;
import pl.lodz.p.library.ports.inbound.UserUseCase;
import pl.lodz.p.library.domain.model.Loan;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.ws.test.server.RequestCreators.withPayload;
import static org.springframework.ws.test.server.ResponseMatchers.noFault;
import static org.springframework.ws.test.server.ResponseMatchers.xpath;

@WebServiceServerTest
@Import({UserEndpoint.class, UserSoapConverter.class})
class UserEndpointTest {

    @Autowired
    private ApplicationContext context;

    @MockitoBean
    private UserUseCase userUseCase;

    private MockWebServiceClient client;

    private static final String NS = "http://pl.lodz.p.library.adapters.soap.dto.user/";
    private static final Map<String, String> NS_MAP = Map.of("ns", NS);

    @BeforeEach
    void setUp() {
        client = MockWebServiceClient.createClient(context);
    }

    private Reader buildReader(String id, String login) {
        Reader r = new Reader(login, "pass", login + "@test.pl", 25);
        r.setId(id);
        r.setActive(true);
        return r;
    }

    @Test
    void getAllUsers_shouldReturnList() {
        when(userUseCase.findAllUsers()).thenReturn(List.of(buildReader("u-1", "jan")));

        client.sendRequest(withPayload(new StringSource(
                "<GetAllUsersRequest xmlns=\"" + NS + "\"/>")))
                .andExpect(noFault())
                .andExpect(xpath("//ns:user/ns:id", NS_MAP).evaluatesTo("u-1"))
                .andExpect(xpath("//ns:user/ns:login", NS_MAP).evaluatesTo("jan"))
                .andExpect(xpath("//ns:user/ns:type", NS_MAP).evaluatesTo("reader"));
    }

    @Test
    void getUserById_shouldReturnUser() {
        when(userUseCase.findUserById("u-42")).thenReturn(buildReader("u-42", "anna"));

        client.sendRequest(withPayload(new StringSource(
                "<GetUserByIdRequest xmlns=\"" + NS + "\"><id>u-42</id></GetUserByIdRequest>")))
                .andExpect(noFault())
                .andExpect(xpath("//ns:user/ns:id", NS_MAP).evaluatesTo("u-42"))
                .andExpect(xpath("//ns:user/ns:login", NS_MAP).evaluatesTo("anna"));
    }

    @Test
    void addUser_shouldReturnCreatedUser() {
        Reader saved = buildReader("u-new", "newuser");
        when(userUseCase.addUser(any(User.class))).thenReturn(saved);

        client.sendRequest(withPayload(new StringSource("""
                <AddUserRequest xmlns="%s">
                    <userDTO>
                        <login>newuser</login>
                        <email>newuser@test.pl</email>
                        <age>25</age>
                        <active>false</active>
                        <type>reader</type>
                        <currentLoansCount>0</currentLoansCount>
                    </userDTO>
                    <password>secret123</password>
                </AddUserRequest>""".formatted(NS))))
                .andExpect(noFault())
                .andExpect(xpath("//ns:user/ns:id", NS_MAP).evaluatesTo("u-new"))
                .andExpect(xpath("//ns:user/ns:login", NS_MAP).evaluatesTo("newuser"));
    }

    @Test
    void activateUser_shouldReturnActivatedUser() {
        when(userUseCase.activateUser("u-inactive")).thenReturn(buildReader("u-inactive", "piotr"));

        client.sendRequest(withPayload(new StringSource(
                "<ActivateUserRequest xmlns=\"" + NS + "\"><id>u-inactive</id></ActivateUserRequest>")))
                .andExpect(noFault())
                .andExpect(xpath("//ns:user/ns:active", NS_MAP).evaluatesTo("true"));
    }

    @Test
    void deleteUser_shouldReturnDeleted() {
        client.sendRequest(withPayload(new StringSource(
                "<DeleteUserRequest xmlns=\"" + NS + "\"><id>u-del</id></DeleteUserRequest>")))
                .andExpect(noFault())
                .andExpect(xpath("//ns:DeleteUserResponse/ns:isDeleted", NS_MAP).evaluatesTo("true"));

        verify(userUseCase).deleteUser("u-del");
    }
}
