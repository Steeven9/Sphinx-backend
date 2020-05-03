package ch.usi.inf.sa4.sphinx.misc;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown to indicate a 401 UNAUTHORIZED
 */
public class UnauthorizedException extends HttpException {
    public UnauthorizedException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
