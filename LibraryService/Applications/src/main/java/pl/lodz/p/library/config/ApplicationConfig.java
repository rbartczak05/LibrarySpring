package pl.lodz.p.library.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import pl.lodz.p.library.ports.outbound.ClientPort;

@Configuration
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class ApplicationConfig {
    private final ClientPort clientPort;

    public ApplicationConfig(ClientPort clientPort) {
        this.clientPort = clientPort;
    }
}