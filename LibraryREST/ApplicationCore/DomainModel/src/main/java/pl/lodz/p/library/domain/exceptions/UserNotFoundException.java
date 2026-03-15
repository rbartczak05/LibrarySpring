package pl.lodz.p.library.domain.exceptions;

public class UserNotFoundException extends AppBaseException {

    public UserNotFoundException(String reason) {
        super(reason);
    }

    public UserNotFoundException(String reason, Throwable cause) {
        super(reason, cause);
    }
}
