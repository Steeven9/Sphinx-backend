package ch.usi.inf.sa4.sphinx.model;


import ch.usi.inf.sa4.sphinx.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Effect that sets a DimmableLight state
 * @see DimmableLight
 */
public class DimmableLightStateSet extends Effect<Double> {

    @Autowired
    private DeviceService deviceService;

    /**
     * Constructor.
     *
     * @param deviceID the id to set the DimmableLightStateSet to
     **/
    public DimmableLightStateSet(Integer deviceID) {
        super(deviceID);
    }



    /**
     * Sets state to the effect value.
     *
     * @param effect: new value of the state
     **/
    public void execute(Double effect) {
        DimmableLight dl = ((DimmableLight) deviceService.get(getDeviceId()).get());
        dl.setState(effect);
        deviceService.update(dl);
    }


}
