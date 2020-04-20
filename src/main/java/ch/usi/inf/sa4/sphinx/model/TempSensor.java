package ch.usi.inf.sa4.sphinx.model;

/**
 * A Temperature sensor measures a temperature (in C) in a given room.
 */
public class TempSensor extends Sensor {

    //future development: support different measurements of temperature.

    /**
     * Creates a temperature sensor set to 20.0 degrees celsius.
     */
    public TempSensor() {
        super(20.0);
    }

    /** Constructor.
     * @param s a TempSensor
     **/
    public TempSensor(TempSensor s) {
        super(s);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TempSensor makeCopy() {
        return new TempSensor(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getPhQuantity() {
        return " °C";
    }
}