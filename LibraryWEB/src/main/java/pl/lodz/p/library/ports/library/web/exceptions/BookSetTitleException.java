package pl.lodz.p.library.ports.library.web.exceptions;

import org.springframework.http.HttpStatusCode;

public class BookSetTitleException extends AppBaseException {

    public BookSetTitleException(HttpStatusCode message) {
        super(message);
    }

    public BookSetTitleException(HttpStatusCode message, String reason) {
        super(message, reason);
    }

    public BookSetTitleException(HttpStatusCode message, String reason, Throwable cause) {
        super(message, reason, cause);
    }
}
