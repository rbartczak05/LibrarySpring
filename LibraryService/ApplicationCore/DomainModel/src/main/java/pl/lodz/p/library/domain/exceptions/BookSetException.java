package pl.lodz.p.library.domain.exceptions;

public class BookSetException extends AppBaseException {

    public BookSetException(String reason) {
        super(reason);
    }

    public BookSetException(String reason, Throwable cause) {
        super(reason, cause);
    }
}
