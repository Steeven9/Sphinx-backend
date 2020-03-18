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

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getPhQuantity() {
        return " C";
    }
}