package pl.lodz.p.library.domain.exceptions;

public class UserException extends AppBaseException {

    public UserException(String reason) {
        super(reason);
    }

    public UserException(String reason, Throwable cause) {
        super(reason, cause);
    }
}
