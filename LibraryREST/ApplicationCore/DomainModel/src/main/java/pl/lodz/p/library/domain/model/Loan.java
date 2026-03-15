package pl.lodz.p.library.domain.model;

import pl.lodz.p.library.domain.exceptions.BookSetException;

import java.time.LocalDateTime;

public class Loan {
    private String id;
    private boolean active;
    private LocalDateTime startTime;
    private LocalDateTime returnTime;
    private LocalDateTime endTime;
    private String readerId;
    private String bookSetId;

    public Loan(String readerId, String bookSetId, LocalDateTime startTime) {
        this.active = true;
        this.startTime = startTime;
        this.returnTime = null;
        this.endTime = startTime.plusDays(30);
        this.readerId = readerId;
        this.bookSetId = bookSetId;
    }

    public Loan(String reader, String bookSet) {
        this(reader, bookSet, LocalDateTime.now());
    }

    public Loan() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
        if (this.returnTime != null && this.returnTime.isBefore(startTime)) {
            throw new BookSetException("Data zwrotu musi być późniejsza niż data rozpoczęcia.");
        }
        if (this.endTime != null && this.endTime.isBefore(startTime)) {
            throw new BookSetException("Data zakończenia musi być późniejsza niż data rozpoczęcia.");
        }
        this.startTime = startTime;
    }

    public LocalDateTime getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(LocalDateTime returnTime) {
        if (returnTime != null && this.startTime != null && returnTime.isBefore(this.startTime)) {
            throw new BookSetException("Data zwrotu musi być późniejsza niż data rozpoczęcia.");
        }
        this.returnTime = returnTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        if (endTime != null && this.startTime != null && endTime.isBefore(this.startTime)) {
            throw new BookSetException("Data zakończenia musi być późniejsza niż data rozpoczęcia.");
        }
        this.endTime = endTime;
    }

    public String getReaderId() {
        return readerId;
    }

    public void setReaderId(String readerId) {
        this.readerId = readerId;
    }

    public String getBookSetId() {
        return bookSetId;
    }

    public void setBookSetId(String bookSetId) {
        this.bookSetId = bookSetId;
    }

    @Override
    public String toString() {
        return "Loan{" + "id=" + id + ", active=" + active + ", startTime=" + startTime + ", returnTime=" + returnTime + ", endTime=" + endTime + ", readerId=" + readerId + ", bookSetId=" + bookSetId + '}';
    }
}