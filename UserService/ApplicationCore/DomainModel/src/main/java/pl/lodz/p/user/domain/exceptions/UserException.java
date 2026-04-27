package pl.lodz.p.user.domain.exceptions;

public class UserException extends AppBaseException {

    public UserException(String reason) {
        super(reason);
    }

    public UserException(String reason, Throwable cause) {
        super(reason, cause);
    }
}
