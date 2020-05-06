package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.misc.ServiceProvider;
import ch.usi.inf.sa4.sphinx.service.DeviceService;
import com.google.gson.annotations.Expose;

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
    @Transient
    private double lastValue;

    private final Random rand = new Random();

    /**
     * @deprecated This constructor should not be used. It exists only for use by the JPA.
     */
    @Deprecated(forRemoval = false)
    public Sensor() {}

    /**
     * Getter for the quantity.
     *
     * @return quantity
     **/
    public double getLastValue() {
        return this.lastValue;
    }

    /**
     * Creates a Sensor with given physical quantity for measurement.
     *
     * @param quantity a physical quantity
     */
    protected Sensor(final double quantity) {
        this.quantity = quantity;
        this.lastValue = quantity;
    }

    public void setLastValue(double lastValue) {
        this.lastValue = lastValue;
    }

    /**
     * Returns the measured physical quantity in given room.
     *
     * @return the physical quantity
     */
    public double getValue() {
        final double variance = rand.nextDouble();
        return this.quantity + variance - 0.5;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabel() {
        return new DecimalFormat("#.##").format(this.getLastValue()) + " " + this.getPhQuantity();
    }

    /**
     * Returns the name of physical quantity of a given sensor.
     * @return the name of physical quantity
     */
    protected abstract String getPhQuantity();
}
