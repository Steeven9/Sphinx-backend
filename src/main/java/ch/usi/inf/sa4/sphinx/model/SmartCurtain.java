package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.misc.DeviceType;

import javax.persistence.Entity;

/**
 * A SmartCurtain is a type of device that can be shadowed.
 */
@Entity
public class SmartCurtain extends Dimmable {

    /**
     * Constructor.
     * Creates a smart curtain that can be shadowed.
     */
    public SmartCurtain() {
        super();
    }

    @Override
    public DeviceType getDeviceType() {
        return DeviceType.SMART_CURTAIN;
    }
}
