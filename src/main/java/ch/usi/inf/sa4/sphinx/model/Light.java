package ch.usi.inf.sa4.sphinx.model;

/**
 * A Light is a normal light that can be either on either off.
 */
public class Light extends Device {

    /**
     * Creates a light.
     */
    public Light() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabel() {
        return this.on ? "on" : "off";
    }
}