package ch.usi.inf.sa4.sphinx.misc;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown to indicate a 403 FORBIDDEN
 */
public class ForbiddenException extends HttpException {
    public ForbiddenException(final String message) {
        super("Forbidden: " + message);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.FORBIDDEN;
    }
}
