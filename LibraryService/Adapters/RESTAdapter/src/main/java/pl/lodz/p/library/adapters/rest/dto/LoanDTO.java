package pl.lodz.p.library.adapters.rest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import pl.lodz.p.library.domain.exceptions.LoanException;

import java.time.LocalDateTime;
import java.util.UUID;

@Relation(collectionRelation = "loans", itemRelation = "loan")
public class LoanDTO extends RepresentationModel<LoanDTO> {
    private UUID id;

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

    @NotNull(message = "ID klienta jest wymagane do utworzenia wypożyczenia.")
    private UUID clientId;

    @NotNull(message = "ID książki jest wymagane do utworzenia wypożyczenia.")
    private UUID bookSetId;

    public LoanDTO() {
    }

    public LoanDTO(UUID id, LocalDateTime startTime, LocalDateTime endTime, LocalDateTime returnTime, UUID bookSetId, UUID clientId) {
        this.id = id;
        this.active = true;
        this.startTime = startTime;
        this.returnTime = returnTime;
        this.endTime = endTime != null ? endTime : startTime.plusDays(30);
        this.clientId = clientId;
        this.bookSetId = bookSetId;
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