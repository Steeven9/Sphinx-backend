package ch.usi.inf.sa4.sphinx.model;


import ch.usi.inf.sa4.sphinx.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Entity;
import javax.persistence.Transient;

/**
 * Effect that sets a DimmableLight state
 * @see DimmableLight
 */
@Entity
public class DimmableLightStateSet extends Effect<Double> {

    @Autowired
    @Transient
    private DeviceService deviceService;

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
        final DimmableLight dl = ((DimmableLight) deviceService.get(getDeviceId()).get());
        dl.setState(effect);
        deviceService.update(dl);
    }


}
