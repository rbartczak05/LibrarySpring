package pl.lodz.p.library.exception;

import org.springframework.http.HttpStatusCode;

public class UserStateException extends AppBaseException {
    public UserStateException(HttpStatusCode message) {
        super(message);
    }

    public UserStateException(HttpStatusCode message, String reason) {
        super(message, reason);
    }

    public UserStateException(HttpStatusCode message, String reason, Throwable cause) {
        super(message, reason, cause);
    }
}
