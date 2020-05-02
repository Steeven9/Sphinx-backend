package ch.usi.inf.sa4.sphinx.view;

import org.springframework.http.HttpStatus;

public class SerialisableException {
    private HttpStatus error;
    private String message;
    private int code;

    public SerialisableException(HttpStatus error, String message) {
        this.error = error;
        this.message = message;
        if(error != null) this.code = error.value();
    }



    //Getters must be left otherwise
    public HttpStatus getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }



    public int getCode() {
        return code;
    }
}

