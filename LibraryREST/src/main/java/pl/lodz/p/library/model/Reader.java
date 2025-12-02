package pl.lodz.p.library.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import pl.lodz.p.library.exception.ReaderIsInactiveException;
import pl.lodz.p.library.exception.ReaderLimitsException;

public class Reader extends User {
    public static final int maxLoans = 5;

    @Min(0)
    @Max(maxLoans)
    private int currentLoansCount = 0;

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

    @Override
    public String toString() {
        return "Reader{} " + super.toString();
    }
}
