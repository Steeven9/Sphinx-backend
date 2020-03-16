package ch.usi.inf.sa4.sphinx.model;


/**
 * A Light is a normal light that can be on or off.
 */
public class Light extends Device {
    private String type;

    /**
     * Creates a light with given type.
     * @param type a type of the light
     */
    public Light(String type) {
        super();
        this.type = type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabel() {
        return this.name + ", " + this.type;
    }
}