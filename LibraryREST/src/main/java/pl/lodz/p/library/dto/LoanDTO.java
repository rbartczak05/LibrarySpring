package pl.lodz.p.library.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.http.HttpStatus;
import pl.lodz.p.library.exception.BookSetTimeException;
import pl.lodz.p.library.exception.ReaderNotFoundException;

import java.time.LocalDateTime;

public class LoanDTO {
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

    public LoanDTO() {
    }

    public LoanDTO(String id, LocalDateTime startTime, LocalDateTime endTime,
                   LocalDateTime returnTime, String bookSetId, String readerId) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.returnTime = returnTime;
        this.bookSetId = bookSetId;
        this.readerId = readerId;
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
}
