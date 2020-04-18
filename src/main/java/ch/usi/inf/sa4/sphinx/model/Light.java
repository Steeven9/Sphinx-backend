package ch.usi.inf.sa4.sphinx.model;

/**
 * A Light is a normal light that can be either on either off.
 */
public class Light extends Device {

    /**
     * Constructor.
     * Creates a light.
     */
    public Light() {
        super();
    }

    /**
     * Constructor.
     * Creates a light.
     * @param l a Light instance
     */
    public Light(Light l) {
        super(l);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabel() {
        return this.on ? "on" : "off";
    }
}