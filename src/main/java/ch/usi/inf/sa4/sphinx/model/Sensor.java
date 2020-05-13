package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.misc.StatusHolder;

import java.text.DecimalFormat;
import java.util.Random;
import javax.persistence.Entity;


/**
 * A sensor is a general sensor that measures physical quantity. It is used by more specific sensors.
 */
@Entity
public abstract class Sensor extends Device implements StatusHolder<Double> {
    private double quantity;

    /**
     * @deprecated
     * This constructor should not be used. It exists only for use by the JPA.
     */
    @Deprecated
    public Sensor() {}

    /**
     * Creates a Sensor with given physical quantity for measurement.
     *
     * @param quantity a physical quantity
     */
    protected Sensor(final double quantity) {
        this.quantity = quantity;
    }


    /** Getter for the quantity.
     * @return quantity
     **/
    private double getQuantity() {
        return quantity;
    }

    @Override
    public Double getStatus() {
        return getQuantity();
    }

    /**
     * Returns the measured physical quantity in given room, with small random error [-0.5, +0.5].
     *
     * @return the physical quantity
     */
    public double getValue() {
        final double variance = new Random().nextDouble();
        return this.quantity + variance - 0.5;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabel() {
        return new DecimalFormat("#.##").format(this.getValue()) + " " + this.getPhQuantity();
    }

    /**
     * Returns the name of physical quantity of a given sensor.
     * @return the name of physical quantity
     */
    protected abstract String getPhQuantity();
}
