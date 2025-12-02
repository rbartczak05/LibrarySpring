package pl.lodz.p.library.exception;

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
