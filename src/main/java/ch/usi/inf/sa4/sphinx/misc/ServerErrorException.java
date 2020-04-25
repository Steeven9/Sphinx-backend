package ch.usi.inf.sa4.sphinx.misc;

import org.springframework.http.HttpStatus;

public class ServerErrorException extends HttpException{
    public ServerErrorException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
