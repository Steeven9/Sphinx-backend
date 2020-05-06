package ch.usi.inf.sa4.sphinx.model;


import org.springframework.lang.NonNull;

/**
 * Event linked to the change of intensity of a DimmableSwitch
 * @see DimmableSwitch
 */
public class DimmSwitchChangedEvent extends Event<Double> {

    /**
     * Constructor.
     *
     * @param deviceID the id to set the DimmSwitchChangedEvent to
     **/
    public DimmSwitchChangedEvent(@NonNull final int deviceID) {
        super(deviceID);
    }


    /**
     * Get's current value of state
     *
     * @return the value of the state of the device
     **/
    @Override
    public Double get() {
        return ((DimmableSwitch) deviceService.get(getDeviceId()).get()).getIntensity();
    }

}
