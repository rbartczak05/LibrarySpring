package pl.lodz.p.library.mq;

import java.util.UUID;

public class ClientCreationRejectedEvent {
    private UUID userId;
    private String reason;

    public ClientCreationRejectedEvent() {
    }

    public ClientCreationRejectedEvent(UUID userId, String reason) {
        this.userId = userId;
        this.reason = reason;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}