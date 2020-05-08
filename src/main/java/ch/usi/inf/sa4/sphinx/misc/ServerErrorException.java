package ch.usi.inf.sa4.sphinx.misc;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown to indicate a 500 INTERNAL_SERVER_ERROR
 */
public class ServerErrorException extends HttpException{
    public ServerErrorException(final String message) {
        super(message);
    }
    public ServerErrorException(final String message, final Exception cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
