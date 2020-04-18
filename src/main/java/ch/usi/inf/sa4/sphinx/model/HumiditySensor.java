package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.misc.DeviceType;

import javax.persistence.Entity;

/**
 * A humidity sensor measures humidity (in %) in a given room.
 */
@Entity
public class HumiditySensor extends Sensor {

    /**
     * Constructor.
     * Creates a humidity sensor set to 32.0 percent.
     */
    public HumiditySensor() {
        super(32.0);
    }



    /**
     * {@inheritDoc}
     */
    @Override
    protected String getPhQuantity() {
        return "%";
    }


    @Override
    protected DeviceType getDeviceType() {
        return DeviceType.HUMIDITY_SENSOR;
    }
}