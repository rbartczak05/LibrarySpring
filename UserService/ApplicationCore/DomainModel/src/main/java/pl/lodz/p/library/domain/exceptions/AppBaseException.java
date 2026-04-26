package pl.lodz.p.library.domain.exceptions;

public abstract class AppBaseException extends RuntimeException {
    private final String reason;

    public AppBaseException(String reason) {
        super(reason);
        this.reason = reason;
    }

    public AppBaseException(String reason, Throwable cause) {
        super(reason, cause);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
