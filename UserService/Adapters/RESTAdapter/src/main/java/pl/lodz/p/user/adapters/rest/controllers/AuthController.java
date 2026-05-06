package pl.lodz.p.user.adapters.rest.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import pl.lodz.p.user.adapters.rest.dto.*;
import pl.lodz.p.user.adapters.rest.security.JwtService;
import pl.lodz.p.user.domain.model.Reader;
import pl.lodz.p.user.domain.model.User;
import pl.lodz.p.user.ports.inbound.UserUseCase;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserUseCase userUseCase;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService, UserUseCase userUseCase, UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userUseCase = userUseCase;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.login(), request.password())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String accessToken = jwtService.generateAccessToken(userDetails);
            String refreshToken = jwtService.generateRefreshToken(userDetails);
            String role = userDetails.getAuthorities().iterator().next().getAuthority();

            return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken, role, request.login()));
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Nieprawidlowy login lub haslo");
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        String refreshToken = request.refreshToken();
        UUID userId = jwtService.extractUsername(refreshToken);
        User user = userUseCase.findUserById(userId);

        if (user != null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getLogin());
            String newAccessToken = jwtService.generateAccessToken(userDetails);
            String role = userDetails.getAuthorities().iterator().next().getAuthority();
            return ResponseEntity.ok(new AuthResponse(newAccessToken, refreshToken, role, user.getLogin()));
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Nieprawidlowy lub wygasly token");
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordRequest request) {
        String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userUseCase.findUserById(currentUserId);

        if (!passwordEncoder.matches(request.oldPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Nie udalo sie zmienic hasla.");
        }

        String encodedNewPassword = passwordEncoder.encode(request.newPassword());
        userUseCase.changeUserPasswordInModel(user, encodedNewPassword);
        userUseCase.addUser(user);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterRequest request) {
        Reader reader = new Reader(
                request.login(),
                passwordEncoder.encode(request.password()),
                request.email(),
                request.firstName(),
                request.lastName(),
                request.age()
        );
        userUseCase.addUser(reader);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}