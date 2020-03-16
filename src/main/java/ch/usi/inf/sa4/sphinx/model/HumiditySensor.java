package ch.usi.inf.sa4.sphinx.model;


/**
 * A humidity sensor measures humidity (in %) in a given room.
 */
public class HumiditySensor extends Sensor {

    /**
     * Creates a humidity sensor set to 32.0 degrees percent.
     */
    public HumiditySensor() {
        super(32.0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getPhQuantity() {
        return " %";
    }
}