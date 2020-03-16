package ch.usi.inf.sa4.sphinx.model;


import java.util.Random;

/**
 * A TempSensor measures a temperature in a given room.
 */
public class TempSensor extends Device {

    //future development: support different measurements of temperature.
    private double temperature;

    /**
     * Creates a temperature sensor set to 20.0 degrees celsius.
     */
    public TempSensor() {
        super();
        this.temperature = 20.0;
    }

    /**
     * Return the temperature of this sensor with small random error.
     * @return the temperature with small random error [-0.5, +0.5]
     */
    public double getTemperature() {
        double var = new Random().nextDouble();
        return temperature - var - 0.5;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabel() {
        return this.name +", " + this.temperature + " C";
    }
}