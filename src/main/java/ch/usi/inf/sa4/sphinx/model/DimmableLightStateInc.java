package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
public class DimmableLightStateInc extends Effect<Double> {
    @Autowired
    @Transient
    DeviceService deviceService;

    /**
     * Constructor.
     *
     * @param deviceID the id to set the DimmableLightStateInc to
     **/
    public DimmableLightStateInc(final Integer deviceID) {
        super(deviceID);
    }



    /**
     * Sets current state of the Device to the given value.
     *
     * @param value: the new state value
     **/
    @Override
    public void execute(final Double value) {
        final DimmableLight light = (DimmableLight) deviceService.get(getDeviceId()).get();
        light.setState(light.getIntensity() + value);
        deviceService.update(light);
    }

}
