package ch.usi.inf.sa4.sphinx.model;

/**
 * A DimmableLight is a type of light that can be dimmable, i.e. support different levels of intensity.
 */
public class DimmableLight extends Dimmable {

    /**
     * Creates a special light that can be dimmable.
     */
    public DimmableLight() {
        super();
    }

    /**
     * Returns the intensity level of this DimmableSwitch.
     * @return the intensity level of this DimmableSwitch
     */
    public double getIntensity() {
        return super.getState();
    }
}