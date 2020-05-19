package ch.usi.inf.sa4.sphinx.model.Coupling;

public class BadCouplingException extends RuntimeException{
    public BadCouplingException() {
        super("Copupling impossible between devices");
    }
}
