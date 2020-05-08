package ch.usi.inf.sa4.sphinx.misc;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown to indicate a 404 NOT_FOUND
 */
public class NotFoundException extends HttpException {
    public NotFoundException(final String message) {
        super("Not found: " + message);
    }
    public NotFoundException(final String message, final Exception cause) {
        super("Not found: " + message, cause);
    }

    @Override
     public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
