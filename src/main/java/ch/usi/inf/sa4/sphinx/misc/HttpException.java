package ch.usi.inf.sa4.sphinx.misc;

import org.springframework.http.HttpStatus;

public abstract class HttpException extends RuntimeException {

    public HttpException(String message) {
        super(message);
    }

    public abstract HttpStatus getStatus();

    public int getCode(){
        return getStatus().value();
    }
}
