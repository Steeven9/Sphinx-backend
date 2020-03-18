package ch.usi.inf.sa4.sphinx.model;

import java.util.Random;

/**
 * A sensor is a general sensor that measures physical quantity. It is used by more specific sensors.
 */
public abstract class Sensor extends Device {
    private double quantity;

    /**
     * Creates a Sensor with given physical quantity for measurement.
     * @param quantity a physical quantity
     */
    protected Sensor(double quantity) {
        super();
        this.quantity = quantity;
    }

    /**
     * Returns the measured physical quantity in given room, with small random error [-0.5, +0.5].
     * @return the physical quantity
     */
    public double getValue() {
        double var = new Random().nextDouble();
        return this.quantity + var - 0.5;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabel() {
        return this.getValue() + " " + this.getPhQuantity();
    }

    /**
     * Returns the name of physical quantity of a given sensor.
     * @return the name of physical quantity
     */
    protected abstract String getPhQuantity();
}
