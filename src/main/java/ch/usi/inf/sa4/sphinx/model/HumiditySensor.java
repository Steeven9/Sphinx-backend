package ch.usi.inf.sa4.sphinx.model;

/**
 * A humidity sensor measures humidity (in %) in a given room.
 */
public class HumiditySensor extends Sensor {

    /**
     * Constructor.
     * Creates a humidity sensor set to 32.0 percent.
     */
    public HumiditySensor() {
        super(32.0);
    }

    /**
     * Constructor.
     * Creates a humidity sensor set to the percent of the HumiditySensor passed.
     * @param s , a HumiditySensor instance
     */
    public HumiditySensor(HumiditySensor s) {
        super(s);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HumiditySensor makeCopy() {
        return new HumiditySensor(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getPhQuantity() {
        return " %";
    }
}