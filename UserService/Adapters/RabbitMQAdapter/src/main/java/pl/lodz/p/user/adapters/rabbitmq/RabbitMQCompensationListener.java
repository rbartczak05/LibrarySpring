package pl.lodz.p.user.adapters.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pl.lodz.p.user.ports.inbound.UserUseCase;

@Component
public class RabbitMQCompensationListener {
    private final UserUseCase userUseCase;

    public RabbitMQCompensationListener(UserUseCase userUseCase) {
        this.userUseCase = userUseCase;
    }

    @RabbitListener(queues = RabbitMQConfig.USER_COMPENSATION_QUEUE)
    public void handleClientCreationRejected(UserCreationRejectEvent event) {
        System.err.println("Kompensata: dezaktywuje usera " + event.getUserId() + " - powod: " + event.getReason());
        try {
            userUseCase.deactivateUser(event.getUserId());
        } catch (Exception e) {
            System.err.println("Kompensata nie powiodla sie dla " + event.getUserId() + ": " + e.getMessage());
        }
    }
}