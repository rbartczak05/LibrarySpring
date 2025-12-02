package pl.lodz.p.library.web.exceptions;

import org.springframework.http.HttpStatusCode;

public class ReaderLimitsException extends AppBaseException {

    public ReaderLimitsException(HttpStatusCode message) {
        super(message);
    }

    public ReaderLimitsException(HttpStatusCode message, String reason) {
        super(message, reason);
    }

    public ReaderLimitsException(HttpStatusCode message, String reason, Throwable cause) {
        super(message, reason, cause);
    }
}
