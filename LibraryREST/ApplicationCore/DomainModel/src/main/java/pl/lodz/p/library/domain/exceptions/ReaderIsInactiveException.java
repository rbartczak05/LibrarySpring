package pl.lodz.p.library.domain.exceptions;

public class ReaderIsInactiveException extends AppBaseException {

    public ReaderIsInactiveException(String reason) {
        super(reason);
    }

    public ReaderIsInactiveException(String reason, Throwable cause) {
        super(reason, cause);
    }
}
