package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.misc.NotImplementedException;

import java.util.Random;

public class TempSensor extends Device {

    //Which temperature measurement do we support?? kelvin, fahrenheit
    private double temperature;

    public TempSensor() {
        super();
        this.temperature = 20.0;
    }

    /**
     * @return the temperature with small random error [-0.5, +0.5]
     */
    public double getTemperature() {
        double var = new Random().nextDouble();
        return temperature - var - 0.5;
    }

    @Override
    public String getLabel() {
        throw new NotImplementedException();
    }
}