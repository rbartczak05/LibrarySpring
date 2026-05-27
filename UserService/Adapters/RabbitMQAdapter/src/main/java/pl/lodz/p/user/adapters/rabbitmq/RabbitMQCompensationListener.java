package pl.lodz.p.user.adapters.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pl.lodz.p.user.ports.outbound.UserPort;

@Component
public class RabbitMQCompensationListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQCompensationListener.class);

    private final UserPort userPort;

    public RabbitMQCompensationListener(UserPort userPort) {
        this.userPort = userPort;
    }

    @RabbitListener(queues = RabbitMQConfig.USER_COMPENSATE_QUEUE)
    public void handleClientCreationRejected(UserCreationRejectEvent event) {
        LOGGER.warn("Kompensacja – otrzymano REJECT dla userId: {}. Powód: {}",
                event.getUserId(), event.getReason());
        try {
            userPort.deleteUser(event.getUserId());
            LOGGER.info("Kompensacja zakończona – user {} usunięty.", event.getUserId());
        } catch (Exception e) {
            LOGGER.error("Błąd podczas kompensacji transakcji dla userId {}: {}",
                    event.getUserId(), e.getMessage());
        }
    }
}