package ch.usi.inf.sa4.sphinx.misc;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException() {
    }

    public UnauthorizedException(String message) {
        super(message);
    }
}
