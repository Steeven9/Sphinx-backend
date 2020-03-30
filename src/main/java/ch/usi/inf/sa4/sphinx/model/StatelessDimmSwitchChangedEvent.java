package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;

public class StatelessDimmSwitchChangedEvent extends Event<Double> {
    public final int device;
    private double increment;
    @Autowired
    private DeviceService deviceService;


    public StatelessDimmSwitchChangedEvent(int deviceID, double increment){
        super(deviceID);
        this.device = deviceID;
        this.increment = increment;
    }

    /** Gets current state of device
     * @return value of state of device **/
    @Override
    public Double get() {
        return ((StatelessDimmableSwitch) deviceService.get(device)).isIncrementing() ? increment : -increment;
    }

    @Override
    public @NotNull Event<Double> makeCopy() {
        return new StatelessDimmSwitchChangedEvent(device, increment);
    }
}
