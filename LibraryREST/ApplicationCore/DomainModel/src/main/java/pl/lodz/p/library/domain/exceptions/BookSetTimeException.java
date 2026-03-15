package pl.lodz.p.library.domain.exceptions;

public class BookSetTimeException extends AppBaseException {

    public BookSetTimeException(String reason) {
        super(reason);
    }

    public BookSetTimeException(String reason, Throwable cause) {
        super(reason, cause);
    }
}
