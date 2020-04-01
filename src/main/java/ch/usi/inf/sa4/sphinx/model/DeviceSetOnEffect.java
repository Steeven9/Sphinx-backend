package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.service.DeviceService;
import ch.usi.inf.sa4.sphinx.service.Storage;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;

public class DeviceSetOnEffect extends Effect< Boolean> {
    public final int device;
    @Autowired
    private DeviceService deviceService;


    public DeviceSetOnEffect(int deviceID){
        super(deviceID);
        this.device = deviceID;
    }

    /** checks if value of lights is off e.g, false; if so it turn them on and vice versa.
     * @param  effect: the current value of the device
     **/
    public void execute(Boolean effect){
            deviceService.get(device).setOn(effect);
    }

    @Override
    public @NotNull Effect<?> makeCopy() {
        return new DeviceSetOnEffect(device);
    }
}
