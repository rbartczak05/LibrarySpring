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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import pl.lodz.p.library.dto.AuthRequest;
import pl.lodz.p.library.dto.AuthResponse;
import pl.lodz.p.library.dto.ChangePasswordRequest;
import pl.lodz.p.library.dto.RegisterRequest;
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

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          JwtService jwtService,
                          UserService userService,
                          PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
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

            String token = jwtService.generateToken(userDetails);

            String role = userDetails.getAuthorities().iterator().next().getAuthority();

            return ResponseEntity.ok(new AuthResponse(token, role, userDetails.getUsername()));

        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Nieprawidłowy login lub hasło");
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordRequest request) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userService.findUserByLogin(currentUsername);

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Stare hasło jest nieprawidłowe.");
        }

        String encodedNewPassword = passwordEncoder.encode(request.getNewPassword());

        userService.changeUserPasswordInModel(user, encodedNewPassword);

        userService.addUser(user);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register( @RequestBody RegisterRequest request) {
        Reader reader = new Reader(request.getLogin(), passwordEncoder.encode(request.getPassword()),
                request.getEmail(), request.getAge());

        userService.addUser(reader);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}