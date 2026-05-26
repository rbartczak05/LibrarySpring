package pl.lodz.p.user.adapters.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pl.lodz.p.user.ports.inbound.UserUseCase;

@Component
public class RabbitMQFaultEventListener {
    private final UserUseCase userUseCase;

    public RabbitMQFaultEventListener(UserUseCase userUseCase) {
        this.userUseCase = userUseCase;
    }

    @RabbitListener(queues = "user.rejected.queue")
    public void handleUserCreationRejected(UserCreationRejectEvent event) {
        System.out.println("Otrzymano sygnał kompensacji z LibraryService! Cofam utworzenie użytkownika o ID: " + event.getUserId());
        //TODO Zamiast System.out lepiej zrobić potem logger z slf4j.
        userUseCase.deactivateUser(event.getUserId());
    }
}