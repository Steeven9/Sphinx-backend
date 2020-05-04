package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.misc.DeviceType;

import javax.persistence.Entity;
import ch.usi.inf.sa4.sphinx.service.CouplingService;
import ch.usi.inf.sa4.sphinx.service.RoomService;

/**
 * A DimmableSwitch is a switch that can set and store the intensity for given device.
 */
@Entity
public class DimmableSwitch extends Dimmable {
    /**
     * Constructor.
     * Creates a dimmable switch.
     */
    public DimmableSwitch() {
    }


    @Override
    public DeviceType getDeviceType() {
        return DeviceType.DIMMABLE_SWITCH;
    }
}