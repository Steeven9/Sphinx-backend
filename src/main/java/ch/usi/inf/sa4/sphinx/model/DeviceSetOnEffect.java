package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.controller.Storage;

public class DeviceSetOnEffect extends Effect< Boolean> {
    public final int device;

    public DeviceSetOnEffect(int deviceID){
        super(deviceID);
        this.device = deviceID;
    }

    /** checks if value of lights is off e.g, false; if so it turn them on and vice versa.
     * @param  effect: the current value of the device
     **/
    public void execute(Boolean effect){
       if (!effect) {
            ((Switch)Storage.getDevice(device)).click();

       }
    }
}
