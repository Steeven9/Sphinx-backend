package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.service.DeviceStorage;
import org.springframework.beans.factory.annotation.Autowired;

public class DeviceSetOnEffect extends Effect< Boolean> {
    public final int device;
    @Autowired
    private DeviceStorage deviceStorage;

    /** Constructor **/
    public DeviceSetOnEffect(int deviceID){
        super(deviceID);
        this.device = deviceID;
    }

    /** Checks if value of lights is off e.g, false; if so it turn them on and vice versa.
     * @param  effect: the current value of the device
     **/
    public void execute(Boolean effect){
            deviceStorage.get(device).setOn(effect);
    }
}
