package pl.lodz.p.library.web.exceptions;

import org.springframework.http.HttpStatusCode;

public class IdException extends AppBaseException {

    public IdException(HttpStatusCode message) {
        super(message);
    }

    public IdException(HttpStatusCode message, String reason) {
        super(message, reason);
    }

    public IdException(HttpStatusCode message, String reason, Throwable cause) {
        super(message, reason, cause);
    }
}
