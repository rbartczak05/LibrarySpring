package pl.lodz.p.library.web.exceptions;

import org.springframework.http.HttpStatusCode;

public class BookSetTimeException extends AppBaseException {

    public BookSetTimeException(HttpStatusCode message) {
        super(message);
    }

    public BookSetTimeException(HttpStatusCode message, String reason, Throwable cause) {
        super(message, reason, cause);
    }

    public BookSetTimeException(HttpStatusCode message, String reason) {
        super(message, reason);
    }
}
