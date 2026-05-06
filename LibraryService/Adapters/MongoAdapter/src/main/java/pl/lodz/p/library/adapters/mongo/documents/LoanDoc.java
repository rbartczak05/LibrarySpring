package pl.lodz.p.library.adapters.mongo.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "loans")
public class LoanDoc {
    @Id
    private UUID id;
    private boolean active;
    private LocalDateTime startTime;
    private LocalDateTime returnTime;
    private LocalDateTime endTime;
    private UUID clientId;
    private UUID bookSetId;

    public LoanDoc(UUID clientId, UUID bookSetId, LocalDateTime startTime) {
        this.active = true;
        this.startTime = startTime;
        this.returnTime = null;
        this.endTime = startTime.plusDays(30);
        this.clientId = clientId;
        this.bookSetId = bookSetId;
    }

    public LoanDoc() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(LocalDateTime returnTime) {
        this.returnTime = returnTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public UUID getClientId() {
        return clientId;
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }

    public UUID getBookSetId() {
        return bookSetId;
    }

    public void setBookSetId(UUID bookSetId) {
        this.bookSetId = bookSetId;
    }
}