package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;

public class DimmableLightStateInc extends Effect<Double> {
    @Autowired
    DeviceService deviceService;

    public DimmableLightStateInc(int deviceID){
        super(deviceID);
    }

    /** Sets current state of the Device to the given value.
     * @param value: the new state value
     **/
    @Override
    public void execute(Double value) {
        DimmableLight light = (DimmableLight) deviceService.get(device);
        light.setState(light.getState() + value);
        deviceService.update(light);
    }

    /**
     * @return a copy of this object
     */
    @Override
    public DimmableLightStateInc makeCopy() {
        return new DimmableLightStateInc(getId());
    }
}
