package ch.usi.inf.sa4.sphinx.model;


import ch.usi.inf.sa4.sphinx.service.DeviceService;
import org.springframework.lang.NonNull;

public class DimmSwitchChangedEvent extends Event<Double> {

    /**
     * Constructor.
     *
     * @param deviceID the id to set the DimmSwitchChangedEvent to
     **/
    public DimmSwitchChangedEvent(@NonNull  int deviceID, DeviceService deviceService) {
        super(deviceID, deviceService);
    }



    /**
     * Get's current value of state
     *
     * @return the value of the state of the device
     **/
    @Override
    public Double get() {
        return ((DimmableSwitch) deviceService.get(deviceId)).getIntensity();
    }

}
