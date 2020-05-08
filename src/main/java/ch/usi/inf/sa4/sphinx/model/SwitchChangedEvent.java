package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.misc.ServiceProvider;
import ch.usi.inf.sa4.sphinx.misc.WrongUniverseException;

import javax.validation.constraints.NotNull;

/**
 * Event associated with the change of the on status of a Switch.
 */
public class SwitchChangedEvent extends Event<Boolean> {


    /**
     * Constructor.
     *
     * @param deviceId the id of a device
     */
    public SwitchChangedEvent(@NotNull final int deviceId) {
        super(deviceId);
    }

    /**
     * Gets current state of device
     *
     * @return true if the switch is on, false otherwise
     **/
    @Override
    public Boolean get() {
        return ServiceProvider.getDeviceService().get(getDeviceId()).orElseThrow(WrongUniverseException::new).isOn();
    }

}
