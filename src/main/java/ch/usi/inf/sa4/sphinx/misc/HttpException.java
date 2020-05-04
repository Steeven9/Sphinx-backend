package ch.usi.inf.sa4.sphinx.misc;

import org.springframework.http.HttpStatus;

/**
 * Exception representing a non success in a https response.
 */
public abstract class HttpException extends RuntimeException {

    public HttpException(final String message) {
        super(message);
    }

    /**
     * @return the status linked to this exception
     * @see HttpStatus
     */
    public abstract HttpStatus getStatus();

    public int getCode(){
        return getStatus().value();
    }
}
