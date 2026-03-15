package pl.lodz.p.library.domain.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import pl.lodz.p.library.domain.exceptions.ReaderIsInactiveException;
import pl.lodz.p.library.domain.exceptions.ReaderLimitsException;

public class Reader extends User {
    public static final int maxLoans = 5;

    @NotNull(message = "Liczba wypożyczeń nie może być wartością null.")
    @Min(value = 0, message = "Czytelnik nie może mieć ujemnej liczby wypożyczeń.")
    @Max(value = maxLoans, message = "Przekroczono maksymalny limit wypożyczeń dla czytelnika (" + maxLoans + ").")
    private int currentLoansCount = 0;

    public Reader(String login, String password, String email, int age) {
        super(login, password, email, age);
    }

    public Reader(String login, String email, int age) {
        super(login, email, age);
    }

    public Reader() {
        super();
    }

    public int getMaxLoans() {
        return maxLoans;
    }

    public int getCurrentLoansCount() {
        return currentLoansCount;
    }

    public void setCurrentLoansCount(int currentLoansCount) {
        if (currentLoansCount > maxLoans || currentLoansCount < 0) {
            throw new ReaderLimitsException("Reader with id: " + getId() + " cannot have more than " + maxLoans + " or less than 0 loans.");
        }
        this.currentLoansCount = currentLoansCount;
    }

    public boolean canBorrowBook() {
        if (!isActive()) {
            throw new ReaderIsInactiveException("Reader with id: " + getId() + " is not active");
        }
        if (currentLoansCount >= maxLoans) {
            throw new ReaderLimitsException("Reader with id: " + getId() + " reached the maximum number of loans.");
        }
        return true;
    }

    @Override
    public String toString() {
        return "Reader{} " + super.toString();
    }
}
