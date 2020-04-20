package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;


public class StatelessDimmSwitchChangedEvent extends Event<Double> {
    private double increment;
    @Autowired
    private DeviceService deviceService;

    /**
     * Constructor.
     *
     * @param deviceID  the id of a device
     * @param increment value for incrementing
     **/
    public StatelessDimmSwitchChangedEvent(@NotNull Integer deviceID, double increment) {
        super(deviceID);
        this.increment = increment;
    }


    /**
     * Gets current state of device
     *
     * @return value of state of device
     **/
    @Override
    public Double get() {
        return ((StatelessDimmableSwitch) deviceService.get(deviceId)).isIncrementing() ? increment : -increment;
    }

}
