package pl.lodz.p.library.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import pl.lodz.p.library.exception.BookSetTimeException;
import pl.lodz.p.library.exception.ReaderNotFoundException;

import java.time.LocalDateTime;

public class LoanDTO {
    @Id
    private String id;
    private boolean active;
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startTime;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime returnTime;
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endTime;
    @NotBlank
    private String readerId;
    @NotBlank
    private String bookSetId;

    public LoanDTO() {
    }

    public LoanDTO(String id, LocalDateTime startTime, LocalDateTime endTime, LocalDateTime returnTime, String bookSetId, String readerId) {
        if (readerId == null || readerId.isEmpty()) {
            throw new ReaderNotFoundException(HttpStatus.CONFLICT, "Reader does not exist");
        }
        if (bookSetId == null || bookSetId.isEmpty()) {
            throw new ReaderNotFoundException(HttpStatus.CONFLICT, "Book set does not exist");
        }
        if (startTime == null) {
            throw new BookSetTimeException(HttpStatus.CONFLICT, "Start time cannot be null");
        }
        this.active = true;
        this.startTime = startTime;
        this.returnTime = null;
        this.endTime = startTime.plusDays(30);
        this.readerId = readerId;
        this.bookSetId = bookSetId;
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
        if (startTime == null) {
            throw new BookSetTimeException(HttpStatus.CONFLICT, "Start time cannot be null");
        }
        if (returnTime != null && returnTime.isBefore(startTime)) {
            throw new BookSetTimeException(HttpStatus.CONFLICT, "Return time must be after start time");
        }
        if (endTime != null && endTime.isBefore(startTime)) {
            throw new BookSetTimeException(HttpStatus.CONFLICT, "End time must be after start time");
        }
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
        if (endTime == null) {
            throw new BookSetTimeException(HttpStatus.CONFLICT, "End time cannot be null");
        }
        if (endTime.isBefore(startTime)) {
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
