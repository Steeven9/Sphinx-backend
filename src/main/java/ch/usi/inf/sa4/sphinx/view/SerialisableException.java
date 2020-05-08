package ch.usi.inf.sa4.sphinx.view;

import lombok.NonNull;
import org.springframework.http.HttpStatus;

public class SerialisableException extends RuntimeException {
    private final HttpStatus status;
    private final String message;
    private final int code;

    public SerialisableException(@NonNull final HttpStatus error, final String message) {
        this.status = error;
        this.message = message;
        this.code = error.value();
    }



    //Getters must be left otherwise
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }



    public int getCode() {
        return code;
    }
}

