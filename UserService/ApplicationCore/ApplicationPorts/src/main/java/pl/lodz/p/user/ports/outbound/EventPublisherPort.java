package pl.lodz.p.user.ports.outbound;

import pl.lodz.p.user.domain.model.User;

public interface EventPublisherPort {
    void publishUserCreatedEvent(User user);
}