package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.controller.Storage;

public class DimmableLightStateInc extends Effect<Double> {
    public final int device;
    public DimmableLightStateInc(int deviceID){
        super(deviceID);
        this.device = deviceID;
    }

    /** Sets current state of the Device to the given value.
     * @param value: the new state value
     **/
    @Override
    public void execute(Double value) {
        ((StatelessDimmableSwitch) Storage.getDevice(device)).setState(value);
    }


}
