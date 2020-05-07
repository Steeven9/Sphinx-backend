package ch.usi.inf.sa4.sphinx.model;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

/**
 * Event associated with the change of the on status of a Switch.
 */
@Entity
public class SwitchChangedEvent extends Event<Boolean> {


    /**
     * Constructor.
     *
     * @param deviceId the id of a device
     */
    public SwitchChangedEvent(@NotNull final int deviceId) {
        super(deviceId);
    }

    private SwitchChangedEvent(final SwitchChangedEvent other) {
        super(other);
    }

    /**
     * Gets current state of device
     *
     * @return true if the switch is on, false otherwise
     **/
    @Override
    public Boolean get() {
        return this.deviceService.get(getDeviceId()).get().isOn();
    }

}
