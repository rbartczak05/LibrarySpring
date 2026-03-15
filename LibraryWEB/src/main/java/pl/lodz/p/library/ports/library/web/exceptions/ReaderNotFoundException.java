package pl.lodz.p.library.ports.library.web.exceptions;

import org.springframework.http.HttpStatusCode;

public class ReaderNotFoundException extends AppBaseException {

    public ReaderNotFoundException(HttpStatusCode message) {
        super(message);
    }

    public ReaderNotFoundException(HttpStatusCode message, String reason) {
        super(message, reason);
    }

    public ReaderNotFoundException(HttpStatusCode message, String reason, Throwable cause) {
        super(message, reason, cause);
    }
}
