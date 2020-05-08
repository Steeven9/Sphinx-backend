package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.misc.ServiceProvider;
import ch.usi.inf.sa4.sphinx.misc.WrongUniverseException;

/**
 * Effect that sets the on status of a Device
 */
public class DeviceSetOnEffect extends Effect<Boolean> {

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
        ServiceProvider.getDeviceService().get(getDeviceId()).orElseThrow(WrongUniverseException::new).setOn(effect);
    }
}
