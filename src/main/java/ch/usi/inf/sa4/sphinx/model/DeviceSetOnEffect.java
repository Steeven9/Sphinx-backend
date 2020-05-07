package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Entity;

/**
 * Effect that sets the on status of a Device
 */
@Entity
public class DeviceSetOnEffect extends Effect<Boolean> {
    @Autowired
    private DeviceService deviceService;


    /**
     * @param deviceID The Device linked to this Effect
     */
    public DeviceSetOnEffect(final int deviceID) {
        super(deviceID);
    }



    /**
     * checks if value of lights is off e.g, false; if so it turn them on and vice versa.
     *
     * @param effect: the current value of the device
     **/
    public void execute(final Boolean effect) {
        deviceService.get(getDeviceId()).get().setOn(effect);
    }
}
