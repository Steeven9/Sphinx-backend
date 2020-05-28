package ch.usi.inf.sa4.sphinx.misc;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown to indicate a 400 BAD_REQUEST
 */
public class BadRequestException extends HttpException {
    public BadRequestException(final String message) {
        super("Bad request: " + message );
    }
    public BadRequestException(final String message, final Exception cause) {
        super("Bad request: " + message, cause);
    }


    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
