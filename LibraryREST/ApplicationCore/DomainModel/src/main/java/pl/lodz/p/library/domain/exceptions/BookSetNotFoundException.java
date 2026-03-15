package pl.lodz.p.library.domain.exceptions;

public class BookSetNotFoundException extends AppBaseException {

    public BookSetNotFoundException(String reason) {
        super(reason);
    }

    public BookSetNotFoundException(String reason, Throwable cause) {
        super(reason, cause);
    }
}
