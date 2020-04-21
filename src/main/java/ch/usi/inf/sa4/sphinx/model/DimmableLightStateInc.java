package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;

public class DimmableLightStateInc extends Effect<Double> {
    @Autowired
    DeviceService deviceService;

    /**
     * Constructor.
     *
     * @param deviceID the id to set the DimmableLightStateInc to
     **/
    public DimmableLightStateInc(Integer deviceID) {
        super(deviceID);
    }



    /**
     * Sets current state of the Device to the given value.
     *
     * @param value: the new state value
     **/
    @Override
    public void execute(Double value) {
        DimmableLight light = (DimmableLight) deviceService.get(deviceId).get();
        light.setState(light.getIntensity() + value);
        deviceService.update(light);
    }

}
