package pl.lodz.p.library.web.exceptions;

import org.springframework.http.HttpStatusCode;

public class BookSetNotFoundException extends AppBaseException {

    public BookSetNotFoundException(HttpStatusCode message) {
        super(message);
    }

    public BookSetNotFoundException(HttpStatusCode message, String reason) {
        super(message, reason);
    }

    public BookSetNotFoundException(HttpStatusCode message, String reason, Throwable cause) {
        super(message, reason, cause);
    }
}