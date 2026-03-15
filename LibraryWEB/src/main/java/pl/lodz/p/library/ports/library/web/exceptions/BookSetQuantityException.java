package pl.lodz.p.library.ports.library.web.exceptions;

import org.springframework.http.HttpStatusCode;

public class BookSetQuantityException extends AppBaseException {

    public BookSetQuantityException(HttpStatusCode message) {
        super(message);
    }

    public BookSetQuantityException(HttpStatusCode message, String reason) {
        super(message, reason);
    }

    public BookSetQuantityException(HttpStatusCode message, String reason, Throwable cause) {
        super(message, reason, cause);
    }
}
