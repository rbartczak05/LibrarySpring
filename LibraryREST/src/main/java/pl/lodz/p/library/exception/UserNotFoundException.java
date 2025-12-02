package pl.lodz.p.library.exception;

import org.springframework.http.HttpStatusCode;

public class UserNotFoundException extends AppBaseException {

    public UserNotFoundException(HttpStatusCode message) {
        super(message);
    }

    public UserNotFoundException(HttpStatusCode message, String reason) {
        super(message, reason);
    }

    public UserNotFoundException(HttpStatusCode message, String reason, Throwable cause) {
        super(message, reason, cause);
    }
}