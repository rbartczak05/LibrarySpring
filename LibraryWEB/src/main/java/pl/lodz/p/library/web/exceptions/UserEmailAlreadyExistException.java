package pl.lodz.p.library.web.exceptions;

import org.springframework.http.HttpStatusCode;

public class UserEmailAlreadyExistException extends AppBaseException {

    public UserEmailAlreadyExistException(HttpStatusCode message) {
        super(message);
    }

    public UserEmailAlreadyExistException(HttpStatusCode message, String reason) {
        super(message, reason);
    }

    public UserEmailAlreadyExistException(HttpStatusCode message, String reason, Throwable cause) {
        super(message, reason, cause);
    }
}