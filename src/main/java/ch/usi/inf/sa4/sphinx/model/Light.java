package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.misc.DeviceType;

import javax.persistence.Entity;
import ch.usi.inf.sa4.sphinx.service.CouplingService;
import ch.usi.inf.sa4.sphinx.service.RoomService;

/**
 * A Light is a normal light that can be either on either off.
 */
@Entity
public class Light extends Device {

    /**
     * Constructor.
     * Creates a light.
     */
    public Light(){
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabel() {
        return this.on ? "on" : "off";
    }

    @Override
    protected DeviceType getDeviceType() {
        return DeviceType.LIGHT;
    }

}