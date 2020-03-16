package ch.usi.inf.sa4.sphinx.model;


/**
 * A light sensor measures the quantity of light (in lumen) in a given room.
 */
public class LightSensor extends Sensor {

    /**
     * Create a light sensor set to 500.0 lumen.
     */
    public LightSensor() {
        super(500.0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getPhQuantity() {
        return " lumen";
    }
}