package pl.lodz.p.library.web.exceptions;

import org.springframework.http.HttpStatusCode;

public class LoanAlreadyInactiveException extends AppBaseException {

    public LoanAlreadyInactiveException(HttpStatusCode message, String reason, Throwable cause) {
        super(message, reason, cause);
    }

    public LoanAlreadyInactiveException(HttpStatusCode message, String reason) {
        super(message, reason);
    }

    public LoanAlreadyInactiveException(HttpStatusCode message) {
        super(message);
    }
}
