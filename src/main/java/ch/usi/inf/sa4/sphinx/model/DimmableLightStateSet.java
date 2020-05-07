package ch.usi.inf.sa4.sphinx.model;


import ch.usi.inf.sa4.sphinx.misc.ServiceProvider;
import ch.usi.inf.sa4.sphinx.misc.WrongUniverseException;
import ch.usi.inf.sa4.sphinx.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Effect that sets a DimmableLight state
 * @see DimmableLight
 */
public class DimmableLightStateSet extends Effect<Double> {

    /**
     * Constructor.
     *
     * @param deviceID the id to set the DimmableLightStateSet to
     **/
    public DimmableLightStateSet(final Integer deviceID) {
        super(deviceID);
    }



    /**
     * Sets state to the effect value.
     *
     * @param effect: new value of the state
     **/
    public void execute(final Double effect) {
        final DimmableLight dl = ((DimmableLight) ServiceProvider.getStaticDeviceService().get(getDeviceId()).orElseThrow(WrongUniverseException::new));
        dl.setState(effect);
        ServiceProvider.getStaticDeviceService().update(dl);
    }


}
