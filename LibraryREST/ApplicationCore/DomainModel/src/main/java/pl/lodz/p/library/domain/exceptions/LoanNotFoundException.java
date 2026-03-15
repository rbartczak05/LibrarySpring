package pl.lodz.p.library.domain.exceptions;

public class LoanNotFoundException extends AppBaseException {

    public LoanNotFoundException(String reason) {
        super(reason);
    }

    public LoanNotFoundException(String reason, Throwable cause) {
        super(reason, cause);
    }
}
