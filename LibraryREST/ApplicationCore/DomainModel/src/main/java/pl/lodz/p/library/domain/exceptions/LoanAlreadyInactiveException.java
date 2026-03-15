package pl.lodz.p.library.domain.exceptions;

public class LoanAlreadyInactiveException extends AppBaseException {

    public LoanAlreadyInactiveException(String reason) {
        super(reason);
    }

    public LoanAlreadyInactiveException(String reason, Throwable cause) {
        super(reason, cause);
    }
}
