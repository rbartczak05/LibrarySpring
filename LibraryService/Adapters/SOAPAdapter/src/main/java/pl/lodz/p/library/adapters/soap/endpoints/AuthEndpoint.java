package pl.lodz.p.library.adapters.soap.endpoints;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import pl.lodz.p.library.adapters.soap.dto.auth.*;
import pl.lodz.p.library.adapters.soap.security.JwtSoapService;
import pl.lodz.p.library.domain.exceptions.AuthException;
import pl.lodz.p.library.domain.model.Reader;

@Endpoint
public class AuthEndpoint {

    private static final String NAMESPACE = "http://pl.lodz.p.library.adapters.soap.dto.auth/";

    private final AuthenticationManager authenticationManager;
    private final JwtSoapService jwtSoapService;
    private final UserUseCase userUseCase;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    public AuthEndpoint(AuthenticationManager authenticationManager, JwtSoapService jwtSoapService,
                        UserUseCase userUseCase, PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtSoapService = jwtSoapService;
        this.userUseCase = userUseCase;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "AuthRequest")
    @ResponsePayload
    public AuthResponse login(@RequestPayload AuthRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword())
        );
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String accessToken = jwtSoapService.generateAccessToken(userDetails);
        String refreshToken = jwtSoapService.generateRefreshToken(userDetails);
        String role = userDetails.getAuthorities().iterator().next().getAuthority();
        return new AuthResponse(accessToken, refreshToken, role, userDetails.getUsername());
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "RefreshTokenRequest")
    @ResponsePayload
    public AuthResponse refreshToken(@RequestPayload RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        String userLogin = jwtSoapService.extractUsername(refreshToken);
        if (userLogin != null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userLogin);
            if (jwtSoapService.isTokenValid(refreshToken, userDetails)) {
                String newAccessToken = jwtSoapService.generateAccessToken(userDetails);
                String role = userDetails.getAuthorities().iterator().next().getAuthority();
                return new AuthResponse(newAccessToken, refreshToken, role, userLogin);
            }
        }
        throw new AuthException("Nieprawidłowy lub wygasły token odświeżania");
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "ChangePasswordRequest")
    @ResponsePayload
    public AuthResponse changePassword(@RequestPayload ChangePasswordRequest request) {
        String login = request.getLogin();
        User user = userUseCase.findUserByLogin(login);
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new AuthException("Nieprawidłowe stare hasło");
        }
        userUseCase.changeUserPasswordInModel(user, passwordEncoder.encode(request.getNewPassword()));
        userUseCase.addUser(user);
        UserDetails userDetails = userDetailsService.loadUserByUsername(login);
        String accessToken = jwtSoapService.generateAccessToken(userDetails);
        String refreshToken = jwtSoapService.generateRefreshToken(userDetails);
        String role = userDetails.getAuthorities().iterator().next().getAuthority();
        return new AuthResponse(accessToken, refreshToken, role, login);
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "registerRequest")
    @ResponsePayload
    public AuthResponse register(@RequestPayload RegisterRequest request) {
        Reader reader = new Reader(
                request.getLogin(),
                passwordEncoder.encode(request.getPassword()),
                request.getEmail(),
                request.getAge()
        );
        userUseCase.addUser(reader);
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getLogin());
        String accessToken = jwtSoapService.generateAccessToken(userDetails);
        String refreshToken = jwtSoapService.generateRefreshToken(userDetails);
        String role = userDetails.getAuthorities().iterator().next().getAuthority();
        return new AuthResponse(accessToken, refreshToken, role, request.getLogin());
    }
}