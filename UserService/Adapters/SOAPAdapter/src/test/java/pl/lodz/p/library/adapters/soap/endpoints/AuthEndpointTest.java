package pl.lodz.p.library.adapters.soap.endpoints;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.webservices.server.WebServiceServerTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.ws.test.server.MockWebServiceClient;
import org.springframework.xml.transform.StringSource;
import pl.lodz.p.library.adapters.soap.security.JwtSoapService;
import pl.lodz.p.library.domain.model.Reader;
import pl.lodz.p.library.ports.inbound.UserUseCase;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.ws.test.server.RequestCreators.withPayload;
import static org.springframework.ws.test.server.ResponseMatchers.noFault;
import static org.springframework.ws.test.server.ResponseMatchers.xpath;

@WebServiceServerTest
@Import({AuthEndpoint.class})
class AuthEndpointTest {

    @Autowired
    private ApplicationContext context;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private UserUseCase userUseCase;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private JwtSoapService jwtSoapService;

    private MockWebServiceClient client;

    private static final String NS = "http://pl.lodz.p.library.adapters.soap.dto.auth/";
    private static final Map<String, String> NS_MAP = Map.of("ns", NS);

    private final User mockUserDetails = new User("jankowalski", "encoded",
            List.of(new SimpleGrantedAuthority("ROLE_READER"))
    );

    @BeforeEach
    void setUp() {
        client = MockWebServiceClient.createClient(context);
    }

    @Test
    void login_shouldReturnTokens() {
        when(jwtSoapService.generateAccessToken(any())).thenReturn("access-token");
        when(jwtSoapService.generateRefreshToken(any())).thenReturn("refresh-token");
        when(authenticationManager.authenticate(any()))
                .thenReturn(new UsernamePasswordAuthenticationToken(
                        mockUserDetails, null, mockUserDetails.getAuthorities()));

        client.sendRequest(withPayload(new StringSource("""
                <AuthRequest xmlns="%s">
                    <login>jankowalski</login>
                    <password>haslo123</password>
                </AuthRequest>""".formatted(NS))))
                .andExpect(noFault())
                .andExpect(xpath("//ns:AuthResponse/ns:login", NS_MAP).evaluatesTo("jankowalski"))
                .andExpect(xpath("//ns:AuthResponse/ns:role", NS_MAP).evaluatesTo("ROLE_READER"));
    }

    @Test
    void register_shouldReturnTokens() {
        Reader reader = new Reader("nowak", "encoded", "nowak@test.pl", 25);
        when(jwtSoapService.generateAccessToken(any())).thenReturn("access-token");
        when(jwtSoapService.generateRefreshToken(any())).thenReturn("refresh-token");
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(userUseCase.addUser(any())).thenReturn(reader);
        when(userDetailsService.loadUserByUsername("nowak")).thenReturn(mockUserDetails);

        client.sendRequest(withPayload(new StringSource("""
                <registerRequest xmlns="%s">
                    <login>nowak</login>
                    <password>haslo123</password>
                    <email>nowak@test.pl</email>
                    <age>25</age>
                </registerRequest>""".formatted(NS))))
                .andExpect(noFault())
                .andExpect(xpath("//ns:AuthResponse/ns:login", NS_MAP).evaluatesTo("nowak"));
    }

    @Test
    void refreshToken_validToken_shouldReturnNewAccessToken() {
        when(jwtSoapService.extractUsername("FAKE_TOKEN")).thenReturn("jankowalski");
        when(userDetailsService.loadUserByUsername("jankowalski")).thenReturn(mockUserDetails);
        when(jwtSoapService.isTokenValid("FAKE_TOKEN", mockUserDetails)).thenReturn(true);
        when(jwtSoapService.generateAccessToken(mockUserDetails)).thenReturn("new-access-token");
        when(jwtSoapService.generateRefreshToken(mockUserDetails)).thenReturn("FAKE_TOKEN");

        client.sendRequest(withPayload(new StringSource("""
            <RefreshTokenRequest xmlns="%s">
                <refreshToken>FAKE_TOKEN</refreshToken>
            </RefreshTokenRequest>""".formatted(NS))))
                .andExpect(noFault())
                .andExpect(xpath("//ns:AuthResponse/ns:login", NS_MAP).evaluatesTo("jankowalski"));
    }

    @Test
    void changePassword_correctOldPassword_shouldReturnTokens() {
        Reader user = new Reader("jankowalski", "encoded_old", "jan@test.pl", 30);
        when(userUseCase.findUserByLogin("jankowalski")).thenReturn(user);
        when(passwordEncoder.matches("stare123", "encoded_old")).thenReturn(true);
        when(passwordEncoder.encode("nowe123")).thenReturn("encoded_new");
        when(userDetailsService.loadUserByUsername("jankowalski")).thenReturn(mockUserDetails);
        when(jwtSoapService.generateAccessToken(any())).thenReturn("access-token");
        when(jwtSoapService.generateRefreshToken(any())).thenReturn("refresh-token");

        client.sendRequest(withPayload(new StringSource("""
                <ChangePasswordRequest xmlns="%s">
                    <login>jankowalski</login>
                    <oldPassword>stare123</oldPassword>
                    <newPassword>nowe123</newPassword>
                </ChangePasswordRequest>""".formatted(NS))))
                .andExpect(noFault())
                .andExpect(xpath("//ns:AuthResponse/ns:login", NS_MAP).evaluatesTo("jankowalski"));

        verify(userUseCase).changeUserPasswordInModel(any(), anyString());
        verify(userUseCase).addUser(any());
    }
}