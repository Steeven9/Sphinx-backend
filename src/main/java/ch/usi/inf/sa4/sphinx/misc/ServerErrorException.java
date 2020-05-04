package ch.usi.inf.sa4.sphinx.misc;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown to indicate a 500 INTERNAL_SERVER_ERROR
 */
public class ServerErrorException extends HttpException{
    public ServerErrorException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
