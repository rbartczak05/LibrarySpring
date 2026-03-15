package pl.lodz.p.library.domain.exceptions;

public class BookSetNotAvailableException extends AppBaseException {

    public BookSetNotAvailableException(String reason) {
        super(reason);
    }

    public BookSetNotAvailableException(String reason, Throwable cause) {
        super(reason, cause);
    }
}
