package ch.usi.inf.sa4.sphinx.model;


/**
 * A DimmableLight is a type of light that can be dimmable, i.e. support different levels of intensity.
 */
public class DimmableLight extends DimmableSwitch {
    private String type;

    /**
     * Creates a special light that can be dimmable, with intensity set to maximum.
     * @param type a String that is a type of the light
     */
    public DimmableLight(String type) {
        super();
        this.type = type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabel() {
        return this.name + " " + this.on + " " + this.type + " " + this.getState();
    }
}