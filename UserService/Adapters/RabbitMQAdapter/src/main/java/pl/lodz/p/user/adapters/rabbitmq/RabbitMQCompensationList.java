package pl.lodz.p.user.adapters.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pl.lodz.p.user.ports.inbound.UserUseCase;

@Component
public class RabbitMQCompensationList {
    private final UserUseCase userUseCase;

    public RabbitMQCompensationListener(UserUseCase userUseCase) {
        this.userUseCase = userUseCase;
    }

    @RabbitListener(queues = RabbitMQConfig.USER_COMPENSATE_QUEUE)
    public void handleClientCreationRejected(ClientCreationRejectedEvent event) {
        System.err.println("Kompensata: usuwam usera " + event.getUserId()
                + " - powód: " + event.getReason());
        try {
            userUseCase.deleteUser(event.getUserId());
        } catch (Exception e) {
            System.err.println("Kompensata nie powiodła się dla " + event.getUserId() + ": " + e.getMessage());
        }
    }
}
