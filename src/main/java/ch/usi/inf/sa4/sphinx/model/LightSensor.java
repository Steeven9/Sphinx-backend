package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.misc.DeviceType;

import javax.persistence.Entity;

/**
 * A light sensor measures the quantity of light (in lumen) in a given room.
 */
@Entity
public class LightSensor extends Sensor {

    /**
     * Create a light sensor set to 500.0 lumen.
     */
    public LightSensor() {
        super(500.0);
    }



    /**
     * {@inheritDoc}
     */
    @Override
    protected String getPhQuantity() {
        return " lm";
    }


    @Override
    protected DeviceType getDeviceType() {
        return DeviceType.LIGHT_SENSOR;
    }
}
