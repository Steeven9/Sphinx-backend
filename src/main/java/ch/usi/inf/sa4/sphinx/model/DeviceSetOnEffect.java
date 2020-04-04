package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;

public class DeviceSetOnEffect extends Effect<Boolean> {
    @Autowired
    private DeviceService deviceService;


    public DeviceSetOnEffect(@NotNull Integer deviceID) {
        super(deviceID);
    }

    /**
     * checks if value of lights is off e.g, false; if so it turn them on and vice versa.
     *
     * @param effect: the current value of the device
     **/
    public void execute(Boolean effect) {
        deviceService.get(getKey()).setOn(effect);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Effect<?> makeCopy() {
        return new DeviceSetOnEffect(getKey());
    }
}
