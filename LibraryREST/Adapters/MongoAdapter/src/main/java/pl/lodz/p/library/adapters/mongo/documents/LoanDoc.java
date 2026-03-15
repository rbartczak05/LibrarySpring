package pl.lodz.p.library.adapters.mongo.documents;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import pl.lodz.p.library.domain.exceptions.LoanException;

import java.time.LocalDateTime;

@Document(collection = "loans")
public class LoanDoc {
    @Id
    private String id;

    @NotNull(message = "Stan wypożyczenia nie może być pusty.")
    private boolean active;

    @NotNull(message = "Data rozpoczęcia wypożyczenia nie może być pusta.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startTime;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime returnTime;

    @NotNull(message = "Data planowanego zakończenia nie może być pusta.")
    @Future(message = "Planowana data zakończenia musi być w przyszłości.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endTime;

    @NotBlank(message = "ID czytelnika jest wymagane do utworzenia wypożyczenia.")
    private String readerId;

    @NotBlank(message = "ID książki jest wymagane do utworzenia wypożyczenia.")
    private String bookSetId;

    public LoanDoc(String readerId, String bookSetId, LocalDateTime startTime) {
        this.active = true;
        this.startTime = startTime;
        this.returnTime = null;
        this.endTime = startTime.plusDays(30);
        this.readerId = readerId;
        this.bookSetId = bookSetId;
    }

    public LoanDoc(String reader, String bookSet) {
        this(reader, bookSet, LocalDateTime.now());
    }

    public LoanDoc() {

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
            throw new LoanException("Data zwrotu musi być późniejsza niż data rozpoczęcia.");
        }
        if (this.endTime != null && this.endTime.isBefore(startTime)) {
            throw new LoanException("Data zakończenia musi być późniejsza niż data rozpoczęcia.");
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