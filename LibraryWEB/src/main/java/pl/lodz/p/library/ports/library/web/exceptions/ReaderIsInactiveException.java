package pl.lodz.p.library.ports.library.web.exceptions;

import org.springframework.http.HttpStatusCode;

public class ReaderIsInactiveException extends AppBaseException {
    public ReaderIsInactiveException(HttpStatusCode message) {
        super(message);
    }

    public ReaderIsInactiveException(HttpStatusCode message, String reason) {
        super(message, reason);
    }

    public ReaderIsInactiveException(HttpStatusCode message, String reason, Throwable cause) {
        super(message, reason, cause);
    }
}
