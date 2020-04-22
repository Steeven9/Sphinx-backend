package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.service.DeviceService;

import javax.validation.constraints.NotNull;

public class SwitchChangedEvent extends Event<Boolean> {


    /**
     * Constructor.
     *
     * @param deviceId the id of a device
     */
    public SwitchChangedEvent(@NotNull int deviceId, DeviceService deviceService) {
        super(deviceId, deviceService);
    }

    private SwitchChangedEvent(SwitchChangedEvent other) {
        super(other);
    }

    /**
     * Gets current state of device
     *
     * @return true if the switch is on, false otherwise
     **/
    @Override
    public Boolean get() {
        return ((Switch) this.deviceService.get(deviceId).get()).isOn();
    }

}
