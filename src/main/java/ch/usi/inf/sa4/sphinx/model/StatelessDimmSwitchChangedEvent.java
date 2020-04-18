package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.service.DeviceService;

import javax.validation.constraints.NotNull;

public class StatelessDimmSwitchChangedEvent extends Event<Double> {
    private double increment;

    /**
     * Constructor.
     *
     * @param deviceID  the id of a device
     * @param increment value for incrementing
     **/
    public StatelessDimmSwitchChangedEvent(@NotNull Integer deviceID, double increment, DeviceService deviceService) {
        super(deviceID, deviceService);
        this.increment = increment;
    }


    /**
     * Gets current state of device
     *
     * @return value of state of device
     **/
    @Override
    public Double get() {
        return ((StatelessDimmableSwitch) this.deviceService.get(deviceId)).isIncrementing() ? increment : -increment;
    }

}
