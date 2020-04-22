package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.misc.DeviceType;

import javax.persistence.Entity;

/**
 * A Light is a normal light that can be either active either off.
 */
@Entity
public class Light extends Device {

    /**
     * Constructor.
     * Creates a light.
     */
    public Light() {
        super();
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabel() {
        return this.on ? "active" : "off";
    }

    @Override
    protected DeviceType getDeviceType() {
        return DeviceType.LIGHT;
    }

}