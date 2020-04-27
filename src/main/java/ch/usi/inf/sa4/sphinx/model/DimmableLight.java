package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.misc.DeviceType;

import javax.persistence.Entity;
import ch.usi.inf.sa4.sphinx.service.CouplingService;
import ch.usi.inf.sa4.sphinx.service.RoomService;

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

    /**
     * Returns the intensity level of this DimmableSwitch.
     * @return the intensity level of this DimmableSwitch
     */
    public double getIntensity() {
        return super.getIntensity();
    }


    @Override
    protected DeviceType getDeviceType() {
        return DeviceType.DIMMABLE_LIGHT;
    }



}