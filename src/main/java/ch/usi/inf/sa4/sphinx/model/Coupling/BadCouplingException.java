package ch.usi.inf.sa4.sphinx.model.Coupling;

public class BadCouplingException extends RuntimeException{
    public BadCouplingException() {
        super("Coupling impossible between devices");
    }
}
