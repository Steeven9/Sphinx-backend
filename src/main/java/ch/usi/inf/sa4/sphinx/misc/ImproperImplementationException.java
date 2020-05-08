package ch.usi.inf.sa4.sphinx.misc;

/**
 * @deprecated
 * Thrown when results obtained from operations return an unexpected result.
 * To be used when writing a new piece of code missing coverage that expects some key behavior to work.
 * Tests should slowly replace this kind of exceptions thus why having this exception in the code will give
 * a deprecated warning.
 */
@Deprecated(forRemoval = false)
public class ImproperImplementationException extends RuntimeException {
    public ImproperImplementationException() {
    }

    public ImproperImplementationException(final String message) {
        super(message);
    }
}
