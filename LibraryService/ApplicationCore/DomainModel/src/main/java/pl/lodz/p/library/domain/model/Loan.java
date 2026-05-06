package pl.lodz.p.library.domain.model;

import pl.lodz.p.library.domain.exceptions.LoanException;

import java.time.LocalDateTime;
import java.util.UUID;

public class Loan {
    private UUID id;

    private boolean active;

    private LocalDateTime startTime;

    private LocalDateTime returnTime;

    private LocalDateTime endTime;

    private UUID clientId;

    private UUID bookSetId;

    public Loan(UUID clientId, UUID bookSetId, LocalDateTime startTime) {
        this.active = true;
        this.startTime = startTime;
        this.returnTime = null;
        this.endTime = startTime.plusDays(30);
        this.clientId = clientId;
        this.bookSetId = bookSetId;
    }

    public Loan(UUID clientId, UUID bookSetId) {
        this(clientId, bookSetId, LocalDateTime.now());
    }

    public Loan() {
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
        if (this.returnTime != null && startTime.isAfter(this.returnTime)) {
            throw new LoanException("Data rozpoczęcia musi być wcześniejsza niż data zwrotu.");
        }
        if (this.endTime != null && startTime.isAfter(this.endTime)) {
            throw new LoanException("Data rozpoczęcia musi być wcześniejsza niż data zakończenia.");
        }
        this.startTime = startTime;
    }

    public LocalDateTime getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(LocalDateTime returnTime) {
        if (returnTime != null && this.startTime != null && returnTime.isBefore(this.startTime)) {
            throw new LoanException("Data zwrotu musi być późniejsza niż data rozpoczęcia.");
        }
        this.returnTime = returnTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        if (endTime != null && this.startTime != null && endTime.isBefore(this.startTime)) {
            throw new LoanException("Data zakończenia musi być późniejsza niż data rozpoczęcia.");
        }
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

    @Override
    public String toString() {
        return "Loan{" +
                "id='" + id + '\'' +
                ", active=" + active +
                ", startTime=" + startTime +
                ", returnTime=" + returnTime +
                ", endTime=" + endTime +
                ", clientId='" + clientId + '\'' +
                ", bookSetId='" + bookSetId + '\'' +
                '}';
    }
}