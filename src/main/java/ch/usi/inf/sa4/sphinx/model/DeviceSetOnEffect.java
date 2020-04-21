package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;

public class DeviceSetOnEffect extends Effect<Boolean> {
    @Autowired
    private DeviceService deviceService;


    public DeviceSetOnEffect(int deviceID) {
        super(deviceID);
    }



    /**
     * checks if value of lights is off e.g, false; if so it turn them active and vice versa.
     *
     * @param effect: the current value of the device
     **/
    public void execute(Boolean effect) {
        deviceService.get(deviceId).get().setActive(effect);
    }
}
