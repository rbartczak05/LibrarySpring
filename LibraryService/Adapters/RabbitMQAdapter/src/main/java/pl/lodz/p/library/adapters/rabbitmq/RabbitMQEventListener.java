package pl.lodz.p.library.adapters.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import pl.lodz.p.library.ports.inbound.ClientUseCase;

@Component
public class RabbitMQEventListener {
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
            System.out.println("Klient utworzony pomyslnie dla ID: " + event.getId());

        } catch (Exception e) {
            System.err.println("Blad tworzenia klienta - inicjuje sage kompensacyjna: " + e.getMessage());

            ClientCreationRejectedEvent rejection = new ClientCreationRejectedEvent(event.getId(), e.getMessage());

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE_NAME,
                    RabbitMQConfig.USER_COMPENSATION_ROUTING_KEY,
                    rejection
            );
        }
    }
}