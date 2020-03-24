package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.controller.Storage;

public class DimmableLightStateSet extends Effect<Double> {
    public final int device;

    public DimmableLightStateSet(int deviceID){
        super(deviceID);
        this.device = deviceID;
    }
    /** Sets state to the effect value.
     * @param effect:  new value of the state
     **/
    public void execute(Double effect){
         ((DimmableLight) Storage.getDevice(device)).setState(effect);

    }
}
