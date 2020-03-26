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

    public Light(Light l) {
        super(l);
    }

    @Override
    public Device makeCopy() {
        return new Light(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabel() {
        return this.on ? "on" : "off";
    }
}