package ch.usi.inf.sa4.sphinx.model;

import java.util.Random;

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
     * Returns the measured physical quantity in given room.
     * @return the physical quantity
     */
    public double getValue() {
        double var = new Random().nextDouble();
        return this.quantity - var - 0.5;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabel() {
        return  this.name +", " + this.getValue() + " " + this.getPhQuantity();
    }

    /**
     * Returns the name of physical quantity of a given sensor.
     * @return the name of physical quantity
     */
    protected abstract String getPhQuantity();
}
