package pl.lodz.p.library.domain.exceptions;

public class ClientException extends AppBaseException {

    public ClientException(String reason) {
        super(reason);
    }

    public ClientException(String reason, Throwable cause) {
        super(reason, cause);
    }
}
