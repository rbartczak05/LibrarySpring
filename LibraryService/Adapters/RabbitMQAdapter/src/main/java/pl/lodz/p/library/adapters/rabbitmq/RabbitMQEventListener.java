package pl.lodz.p.library.adapters.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import pl.lodz.p.library.ports.inbound.ClientUseCase;

@Component
public class RabbitMQEventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQEventListener.class);

    private final ClientUseCase clientUseCase;
    private final RabbitTemplate rabbitTemplate;

    public RabbitMQEventListener(ClientUseCase clientUseCase, RabbitTemplate rabbitTemplate) {
        this.clientUseCase = clientUseCase;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = RabbitMQConfig.USER_CREATED_QUEUE)
    public void handleUserCreated(UserCreatedEvent event) {
        try {
            clientUseCase.registerClientFromEvent(
                    event.getId(),
                    event.getFirstName(),
                    event.getLastName(),
                    event.getEmail(),
                    event.getAge()
            );
            LOGGER.info("Klient utworzony pomyślnie dla ID: {}", event.getId());

        } catch (Exception e) {
            LOGGER.error("Błąd tworzenia klienta - inicjuję sagę kompensacyjną: {}", e.getMessage());

            ClientCreationRejectedEvent rejection =
                    new ClientCreationRejectedEvent(event.getId(), e.getMessage());
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.USER_COMPENSATE_EXCHANGE,
                    RabbitMQConfig.USER_COMPENSATE_ROUTING_KEY,
                    rejection
            );
        }
    }
}