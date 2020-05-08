package ch.usi.inf.sa4.sphinx.misc;

public class WrongUniverseException extends RuntimeException {
    /**
     * Creates a new WrongUniverseException
     */
    public WrongUniverseException() {
        super("The universe broke.\nThe application is in a state that should be impossible to reach.");
    }
}
