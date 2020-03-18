package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.controller.Storage;

public class DimmSwitchChangedEvent extends Event<Double> {
    public final int device;

    public DimmSwitchChangedEvent(int deviceID){
        super(deviceID);
        this.device = deviceID;
    }

    /** Get's current value of state
     * @return Double, the value of the state of the device
     **/
    @Override
    public Double get() {
        return ((DimmableSwitch)Storage.getDevice(device)).getState();
    }
}
