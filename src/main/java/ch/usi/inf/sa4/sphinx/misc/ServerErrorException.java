package ch.usi.inf.sa4.sphinx.misc;

public class ServerErrorException extends RuntimeException{
    public ServerErrorException() {
    }

    public ServerErrorException(String message) {
        super(message);
    }
}
