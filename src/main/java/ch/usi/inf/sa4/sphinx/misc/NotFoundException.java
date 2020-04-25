package ch.usi.inf.sa4.sphinx.misc;

import org.springframework.http.HttpStatus;

public class NotFoundException extends HttpException {
    public NotFoundException(String message) {
        super("Not found: " + message);
    }

    @Override
     public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
