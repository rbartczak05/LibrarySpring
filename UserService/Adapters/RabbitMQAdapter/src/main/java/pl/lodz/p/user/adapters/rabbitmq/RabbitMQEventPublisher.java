package pl.lodz.p.user.adapters.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import pl.lodz.p.user.domain.model.User;
import pl.lodz.p.user.ports.outbound.EventPublisherPort;

@Component
public class RabbitMQEventPublisher implements EventPublisherPort {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publishUserCreatedEvent(User user) {
        UserCreatedEvent event = new UserCreatedEvent(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getAge()
        );
        rabbitTemplate.convertAndSend(RabbitMQConfig.USER_CREATED_EXCHANGE, RabbitMQConfig.USER_CREATED_KEY, event);
    }
}