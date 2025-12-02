package pl.lodz.p.library.exception;

import org.springframework.http.HttpStatusCode;

public class LoanNotFoundException extends AppBaseException {

    public LoanNotFoundException(HttpStatusCode message, String reason, Throwable cause) {
        super(message, reason, cause);
    }

    public LoanNotFoundException(HttpStatusCode message, String reason) {
        super(message, reason);
    }

    public LoanNotFoundException(HttpStatusCode message) {
        super(message);
    }
}
