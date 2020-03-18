package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.controller.Storage;

public class StatelessDimmSwitchChangedEvent extends Event<Double> {
    public final int device;
    private double increment;

    public StatelessDimmSwitchChangedEvent(int deviceID, double increment){
        super(deviceID);
        this.device = deviceID;
        this.increment = increment;
    }

    /** Gets current state of device
     * @return Double, value of state of device **/
    @Override
    public Double get() {
        return ((StatelessDimmableSwitch) Storage.getDevice(device)).isIncrementing() ? increment : -increment;
    }

}
