package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.misc.ServiceProvider;
import ch.usi.inf.sa4.sphinx.misc.WrongUniverseException;
import ch.usi.inf.sa4.sphinx.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;

public class DimmableLightStateInc extends Effect<Double> {

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
        final DimmableLight light = (DimmableLight) ServiceProvider.getStaticDeviceService().get(getDeviceId()).orElseThrow(WrongUniverseException::new);
        light.setState(light.getIntensity() + value);
        ServiceProvider.getStaticDeviceService().update(light);
    }

}
