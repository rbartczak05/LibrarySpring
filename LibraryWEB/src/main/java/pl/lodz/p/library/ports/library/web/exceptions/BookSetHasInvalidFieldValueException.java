package pl.lodz.p.library.ports.library.web.exceptions;

import org.springframework.http.HttpStatusCode;

public class BookSetHasInvalidFieldValueException extends AppBaseException {
    public BookSetHasInvalidFieldValueException(HttpStatusCode message) {
        super(message);
    }

    public BookSetHasInvalidFieldValueException(HttpStatusCode message, String reason) {
        super(message, reason);
    }

    public BookSetHasInvalidFieldValueException(HttpStatusCode message, String reason, Throwable cause) {
        super(message, reason, cause);
    }
}
