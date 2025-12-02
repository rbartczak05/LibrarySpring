package pl.lodz.p.library.exception;

import org.springframework.http.HttpStatusCode;

public class UserHasInvalidFieldValueException extends AppBaseException {
    public UserHasInvalidFieldValueException(HttpStatusCode message) {
        super(message);
    }

    public UserHasInvalidFieldValueException(HttpStatusCode message, String reason) {
        super(message, reason);
    }

    public UserHasInvalidFieldValueException(HttpStatusCode message, String reason, Throwable cause) {
        super(message, reason, cause);
    }
}
