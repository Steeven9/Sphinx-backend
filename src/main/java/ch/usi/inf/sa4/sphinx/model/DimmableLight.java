package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.misc.DeviceType;

import javax.persistence.Entity;

/**
 * A DimmableLight is a type of light that can be dimmable, i.e. support different levels of intensity.
 */
@Entity
public class DimmableLight extends Dimmable {

    /**
     * Constructor
     * Creates a special light that can be dimmable.
     */
    public DimmableLight() {
    }

    @Override
    public DeviceType getDeviceType() {
        return DeviceType.DIMMABLE_LIGHT;
    }



}