package pl.lodz.p.user.domain.exceptions;

public class AuthException extends AppBaseException {
    public AuthException(String reason) {
        super(reason);
    }
    public AuthException(String reason, Throwable cause) {
        super(reason, cause);
    }
}
