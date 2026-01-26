package pl.lodz.p.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
import pl.lodz.p.library.dto.*;
import pl.lodz.p.library.model.Reader;
import pl.lodz.p.library.model.User;
import pl.lodz.p.library.security.JwtService;
import pl.lodz.p.library.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          JwtService jwtService,
                          UserService userService,
                          PasswordEncoder passwordEncoder,
                          UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        try {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getLogin(),
                            request.getPassword()
                    )
            );

            UserDetails userDetails = (UserDetails) authenticate.getPrincipal();

            String accessToken = jwtService.generateAccessToken(userDetails);
            String refreshToken = jwtService.generateRefreshToken(userDetails);

            String role = userDetails.getAuthorities().iterator().next().getAuthority();

            return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken, role, userDetails.getUsername()));

        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Nieprawidłowy login lub hasło");
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        String userLogin = jwtService.extractUsername(refreshToken);

        if (userLogin != null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userLogin);

            if (jwtService.isTokenValid(refreshToken, userDetails)) {
                String newAccessToken = jwtService.generateAccessToken(userDetails);

                String role = userDetails.getAuthorities().iterator().next().getAuthority();

                return ResponseEntity.ok(new AuthResponse(newAccessToken, refreshToken, role, userLogin));
            }
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Nieprawidłowy lub wygasły token odświeżania");
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordRequest request) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findUserByLogin(currentUsername);
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Stare hasło jest nieprawidłowe.");
        }
        String encodedNewPassword = passwordEncoder.encode(request.getNewPassword());
        userService.changeUserPasswordInModel(user, encodedNewPassword);
        userService.addUser(user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterRequest request) {
        Reader reader = new Reader(request.getLogin(), passwordEncoder.encode(request.getPassword()),
                request.getEmail(), request.getAge());
        userService.addUser(reader);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}