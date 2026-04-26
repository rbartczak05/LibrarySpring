package pl.lodz.p.library.domain.exceptions;

public class LoanException extends AppBaseException {

    public LoanException(String reason) {
        super(reason);
    }

    public LoanException(String reason, Throwable cause) {
        super(reason, cause);
    }
}
