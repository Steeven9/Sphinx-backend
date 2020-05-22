package ch.usi.inf.sa4.sphinx.model;


import ch.usi.inf.sa4.sphinx.view.SerialisableDevice;

import java.text.DecimalFormat;
import java.util.Random;
import javax.persistence.Entity;
import javax.persistence.Transient;


/**
 * A sensor is a general sensor that measures physical quantity. It is used by more specific sensors.
 */
@Entity
public abstract class Sensor extends Device {
    private double quantity;
    private double lastValue;
    private double tolerance;
    @Transient
    private final Random rand = new Random();

    /**
     * @deprecated This constructor should not be used. It exists only for use by the JPA.
     */
    @Deprecated(forRemoval = false)
    public Sensor() {}

    /**
     * Creates a Sensor with given physical quantity for measurement.
     *
     * @param quantity a physical quantity
     */
    protected Sensor(final double quantity) {
        this.quantity = quantity;
        this.lastValue = quantity;
        this.tolerance = 1.0;
    }

    /**
     * Returns fix quantity of this sensor.
     *
     * @return quantity of this sensor
     */
    public double getQuantity() {
        return quantity;
    }

    /**
     * Getter for the quantity.
     *
     * @return quantity
     **/
    public double getLastValue() {
        return this.lastValue;
    }

    /**
     * Sets the quantity for this Sensor, i.e. changes its internal state.
     *
     * @param quantity quantity to be set
     */
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    /**
     * Returns the tolerance of error of this sensor.
     *
     * @return the tolerance
     */
    public double getTolerance() {
        return tolerance;
    }


    /**
     * Sets the tolerance of error for given sensor; [-{@code tolerance}, {@code tolerance}].
     *
     * @param tolerance the tolerance of error
     */
    public void setTolerance(double tolerance) {
        if (tolerance < 0) {
            throw new IllegalArgumentException("tolerance should be greater than 0");
        }
        this.tolerance = tolerance;
    }

    /**
     * Sets the physical quantity in given room with a random error set by user.
     */
    public void generateValue() {
        double variance = rand.nextDouble() * this.tolerance * 2;
        this.lastValue = this.quantity + variance - this.tolerance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabel() {
        return new DecimalFormat("#.##").format(this.getLastValue()) + " " + this.getPhQuantity();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SerialisableDevice serialise() {
        final SerialisableDevice sd = super.serialise();
        sd.setTolerance(this.getTolerance());
        sd.setQuantity(this.getQuantity());
        return sd;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPropertiesFrom(SerialisableDevice sd) {
        super.setPropertiesFrom(sd);
        if (sd.getTolerance() != null) this.setTolerance(sd.getTolerance());
        if (sd.getQuantity() != null) this.setQuantity(sd.getQuantity());
    }

    /**
     * Returns the name of physical quantity of a given sensor.
     *
     * @return the name of physical quantity
     */
    protected abstract String getPhQuantity();
}
