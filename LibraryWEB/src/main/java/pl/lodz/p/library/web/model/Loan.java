package pl.lodz.p.library.web.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.http.HttpStatus;
import pl.lodz.p.library.web.exceptions.BookSetTimeException;
import pl.lodz.p.library.web.exceptions.ReaderNotFoundException;

import java.time.LocalDateTime;

@Document(collection = "loans")
public class Loan {
    @Id
    private String id;
    private boolean active;
    @NotNull
    private LocalDateTime startTime;
    private LocalDateTime returnTime;
    private LocalDateTime endTime;
    @NotBlank
    private String readerId;
    @NotBlank
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
        this.startTime = startTime;
    }

    public LocalDateTime getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(LocalDateTime returnTime) {
        if (returnTime != null && returnTime.isBefore(startTime)) {
            throw new BookSetTimeException(HttpStatus.CONFLICT, "Return time must be after start time");
        }
        this.returnTime = returnTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        if (endTime != null && endTime.isBefore(startTime)) {
            throw new BookSetTimeException(HttpStatus.CONFLICT, "End time must be after start time");
        }
        this.endTime = endTime;
    }

    public String getReaderId() {
        return readerId;
    }

    public void setReaderId(String readerId) {
        if (readerId != null && readerId.isEmpty()) {
            throw new ReaderNotFoundException(HttpStatus.CONFLICT, "Reader does not exist");
        }
        this.readerId = readerId;
    }

    public String getBookSetId() {
        return bookSetId;
    }

    public void setBookSetId(String bookSetId) {
        if (bookSetId != null && bookSetId.isEmpty()) {
            throw new ReaderNotFoundException(HttpStatus.CONFLICT, "Book set does not exist");
        }
        this.bookSetId = bookSetId;
    }

    @Override
    public String toString() {
        return "Loan{" +
                "id=" + id +
                ", active=" + active +
                ", startTime=" + startTime +
                ", returnTime=" + returnTime +
                ", endTime=" + endTime +
                ", readerId=" + readerId +
                ", bookSetId=" + bookSetId +
                '}';
    }
}
