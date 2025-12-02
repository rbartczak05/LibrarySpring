package pl.lodz.p.library.exception;

import org.springframework.http.HttpStatusCode;

public class UserLoginAlreadyExistException extends AppBaseException {

    public UserLoginAlreadyExistException(HttpStatusCode message) {
        super(message);
    }

    public UserLoginAlreadyExistException(HttpStatusCode message, String reason) {
        super(message, reason);
    }

    public UserLoginAlreadyExistException(HttpStatusCode message, String reason, Throwable cause) {
        super(message, reason, cause);
    }
}
