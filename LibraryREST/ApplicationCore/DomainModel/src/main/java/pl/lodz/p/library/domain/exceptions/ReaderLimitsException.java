package pl.lodz.p.library.domain.exceptions;

public class ReaderLimitsException extends AppBaseException {

    public ReaderLimitsException(String reason) {
        super(reason);
    }

    public ReaderLimitsException(String reason, Throwable cause) {
        super(reason, cause);
    }
}
