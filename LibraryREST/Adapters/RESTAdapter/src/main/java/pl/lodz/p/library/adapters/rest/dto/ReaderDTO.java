package pl.lodz.p.library.adapters.rest.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import pl.lodz.p.library.exception.ReaderIsInactiveException;
import pl.lodz.p.library.exception.ReaderLimitsException;

public class ReaderDTO extends UserDTO {
    private final static int maxLoans = 5;

    @NotNull(message = "Liczba wypożyczeń nie może być wartością null.")
    @Min(value = 0, message = "Czytelnik nie może mieć ujemnej liczby wypożyczeń.")
    @Max(value = maxLoans, message = "Przekroczono maksymalny limit wypożyczeń dla czytelnika (" + maxLoans + ").")
    private int currentLoansCount = 0;

    public ReaderDTO() {
    }

    public ReaderDTO(String id, String login, String email, int age, boolean active, String type,
                     int currentLoansCount) {
        super(id, login, email, age, active, type);
        this.currentLoansCount = currentLoansCount;
    }

    public int getMaxLoans() {
        return maxLoans;
    }

    public int getCurrentLoansCount() {
        return currentLoansCount;
    }

    public void setCurrentLoansCount(int currentLoansCount) {
        if (currentLoansCount >= maxLoans || currentLoansCount < 0) {
            throw new ReaderLimitsException(HttpStatus.CONFLICT, "Reader with id: " + getId() + " cannot have more than " + maxLoans + " or less than 0 loans.");
        }
        this.currentLoansCount = currentLoansCount;
    }

    public boolean canBorrowBook() {
        if (!isActive()) {
            throw new ReaderIsInactiveException(HttpStatus.CONFLICT, "Reader with id: " + getId() + " is not active");
        }
        if (currentLoansCount >= maxLoans) {
            throw new ReaderLimitsException(HttpStatus.CONFLICT, "Reader with id: " + getId() + " reached the maximum number of loans.");
        }
        return true;
    }
}
