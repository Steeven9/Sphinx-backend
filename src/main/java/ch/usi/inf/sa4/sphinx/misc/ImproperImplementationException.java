package ch.usi.inf.sa4.sphinx.misc;

/**
 * Thrown when results obtained from operations return an unexpected result.
 */
//Tests should slowly replace this exceptions
public class ImproperImplementationException extends RuntimeException {
    public ImproperImplementationException() {
    }

    public ImproperImplementationException(String message) {
        super(message);
    }
}
