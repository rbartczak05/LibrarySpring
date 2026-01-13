package pl.lodz.p.library.exception;

import org.springframework.http.HttpStatusCode;

public class BookSetTimeException extends AppBaseException {

    public BookSetTimeException(HttpStatusCode message) {
        super(message);
    }

    public BookSetTimeException(HttpStatusCode message, String reason) {
        super(message, reason);
    }

    public BookSetTimeException(HttpStatusCode message, String reason, Throwable cause) {
        super(message, reason, cause);
    }
}
