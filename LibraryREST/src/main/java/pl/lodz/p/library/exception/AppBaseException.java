package pl.lodz.p.library.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public abstract class AppBaseException extends ResponseStatusException {

    public AppBaseException(HttpStatusCode message) {
        super(message);
    }

    public AppBaseException(HttpStatusCode message, String reason) {
        super(message, reason);
    }

    public AppBaseException(HttpStatusCode message, String reason, Throwable cause) {
        super(message, reason, cause);
    }
}
