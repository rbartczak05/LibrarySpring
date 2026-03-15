package pl.lodz.p.library.ports.library.web.exceptions;

import org.springframework.http.HttpStatusCode;

public class BookSetNotAvailableException extends AppBaseException {

    public BookSetNotAvailableException(HttpStatusCode message) {
        super(message);
    }

    public BookSetNotAvailableException(HttpStatusCode message, String reason) {
        super(message, reason);
    }

    public BookSetNotAvailableException(HttpStatusCode message, String reason, Throwable cause) {
        super(message, reason, cause);
    }
}
