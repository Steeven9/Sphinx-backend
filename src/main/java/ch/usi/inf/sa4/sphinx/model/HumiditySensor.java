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
     * Given a HumiditySensor, creates a copy of it.
     * @param s The HumiditySensor to copy
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