package pl.lodz.p.user.adapters.soap.endpoints;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.webservices.server.WebServiceServerTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.ws.test.server.MockWebServiceClient;
import org.springframework.xml.transform.StringSource;
import pl.lodz.p.user.adapters.soap.converters.UserSoapConverter;
import pl.lodz.p.user.domain.model.Reader;
import pl.lodz.p.user.domain.model.User;
import pl.lodz.p.user.ports.inbound.UserUseCase;

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

    private static final String NS = "http://pl.lodz.p.user.adapters.soap.dto.user/";
    private static final Map<String, String> NS_MAP = Map.of("ns", NS);

    @Autowired
    private ApplicationContext context;

    @MockitoBean
    private UserUseCase userUseCase;

    private MockWebServiceClient client;

    @BeforeEach
    void setUp() {
        client = MockWebServiceClient.createClient(context);
    }

    private Reader buildReader(UUID id, String login) {
        Reader r = new Reader(login, "pass", "test@example.com", "Tomasz", "Zieliński", 25);
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
                .andExpect(xpath("//ns:user/ns:accessLevel", NS_MAP).evaluatesTo("READER"));
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
    void getUserByLogin_shouldReturnUser() {
        when(userUseCase.findUserByLogin("anna")).thenReturn(buildReader("u-42", "anna"));

        client.sendRequest(withPayload(new StringSource(
                        "<GetUserByLoginRequest xmlns=\"" + NS + "\"><login>anna</login></GetUserByLoginRequest>")))
                .andExpect(noFault())
                .andExpect(xpath("//ns:user/ns:id", NS_MAP).evaluatesTo("u-42"))
                .andExpect(xpath("//ns:user/ns:login", NS_MAP).evaluatesTo("anna"));
    }

    @Test
    void getUserByEmail_shouldReturnUser() {
        when(userUseCase.findUserByEmail("test@example.com")).thenReturn(buildReader("u-42", "anna"));

        client.sendRequest(withPayload(new StringSource(
                        "<GetUserByEmailRequest xmlns=\"" + NS + "\"><email>test@example.com</email></GetUserByEmailRequest>")))
                .andExpect(noFault())
                .andExpect(xpath("//ns:user/ns:id", NS_MAP).evaluatesTo("u-42"))
                .andExpect(xpath("//ns:user/ns:login", NS_MAP).evaluatesTo("anna"));
    }

    @Test
    void addUser_shouldReturnCreatedUser() {
        Reader saved = buildReader("u-new", "newuser");
        when(userUseCase.addUser(any(User.class))).thenReturn(saved);

        client.sendRequest(withPayload(new StringSource("""
            <addUserRequest xmlns="%s">
                <login>newuser</login>
                <password>secret123</password>
                <email>newuser@test.pl</email>
                <firstName>Jan</firstName>
                <lastName>Kowalski</lastName>
                <age>25</age>
                <accessLevel>READER</accessLevel>
            </addUserRequest>""".formatted(NS))))
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
    void deactivateUser_shouldReturnDeactivatedUser() {
        Reader deactivated = buildReader("u-active", "piotr");
        deactivated.setActive(false);
        when(userUseCase.deactivateUser("u-active")).thenReturn(deactivated);

        client.sendRequest(withPayload(new StringSource(
                        "<DeactivateUserRequest xmlns=\"" + NS + "\"><id>u-active</id></DeactivateUserRequest>")))
                .andExpect(noFault())
                .andExpect(xpath("//ns:user/ns:active", NS_MAP).evaluatesTo("false"));
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