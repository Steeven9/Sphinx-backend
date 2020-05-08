package ch.usi.inf.sa4.sphinx.model;


import ch.usi.inf.sa4.sphinx.misc.ServiceProvider;
import ch.usi.inf.sa4.sphinx.misc.WrongUniverseException;
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
        return ((DimmableSwitch) ServiceProvider.getStaticDeviceService().get(getDeviceId())
                .orElseThrow(WrongUniverseException::new)).getIntensity();
    }

}
