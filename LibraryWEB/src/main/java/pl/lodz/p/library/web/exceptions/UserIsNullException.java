package pl.lodz.p.library.web.exceptions;

import org.springframework.http.HttpStatusCode;

public class UserIsNullException extends AppBaseException {
    public UserIsNullException(HttpStatusCode message) {
        super(message);
    }

    public UserIsNullException(HttpStatusCode message, String reason) {
        super(message, reason);
    }

    public UserIsNullException(HttpStatusCode message, String reason, Throwable cause) {
        super(message, reason, cause);
    }
}
