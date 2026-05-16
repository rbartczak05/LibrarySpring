package pl.lodz.p.user.config;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.lodz.p.user.domain.model.Administrator;
import pl.lodz.p.user.domain.model.Librarian;
import pl.lodz.p.user.domain.model.User;
import pl.lodz.p.user.ports.outbound.UserPort;

@Configuration
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class ApplicationConfig {
    private final UserPort userPort;

    public ApplicationConfig(UserPort userPort) {
        this.userPort = userPort;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userPort.findUserByLogin(username)
                .map(user -> org.springframework.security.core.userdetails.User.builder()
                        .username(user.getId().toString())
                        .password(user.getPassword())
                        .roles(mapRole(user))
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("Użytkownik nie znaleziony: " + username));
    }

    private String mapRole(User user) {
        if (user instanceof Administrator) return "ADMIN";
        if (user instanceof Librarian) return "LIBRARIAN";
        return "READER";
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }
}