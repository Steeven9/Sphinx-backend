package ch.usi.inf.sa4.sphinx.model;


import ch.usi.inf.sa4.sphinx.service.DeviceService;

import javax.validation.constraints.NotNull;

public class DimmSwitchChangedEvent extends Event<Double> {

    /**
     * Constructor.
     *
     * @param deviceID the id to set the DimmSwitchChangedEvent to
     **/
    public DimmSwitchChangedEvent(@NotNull int deviceID, DeviceService deviceService) {
        super(deviceID, deviceService);
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
        return ((DimmableSwitch) this.deviceService.get(deviceId)).getState();
    }

}
