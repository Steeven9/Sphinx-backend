package ch.usi.inf.sa4.sphinx.model;


import ch.usi.inf.sa4.sphinx.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;

public class DimmSwitchChangedEvent extends Event<Double> {

    @Autowired
    private DeviceService deviceService;

    /**
     * Constructor.
     *
     * @param deviceID the id to set the DimmSwitchChangedEvent to
     **/
    public DimmSwitchChangedEvent(int deviceID) {
        super(deviceID);
    }

    private DimmSwitchChangedEvent(DimmSwitchChangedEvent other){
        super(other);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public DimmSwitchChangedEvent makeCopy() {
        return new DimmSwitchChangedEvent(this);
    }


    /**
     * Get's current value of state
     *
     * @return the value of the state of the device
     **/
    @Override
    public Double get() {
        return ((DimmableSwitch) deviceService.get(deviceId)).getState();
    }

}
