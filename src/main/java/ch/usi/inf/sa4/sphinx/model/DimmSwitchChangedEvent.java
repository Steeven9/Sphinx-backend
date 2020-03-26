package ch.usi.inf.sa4.sphinx.model;


import ch.usi.inf.sa4.sphinx.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;

public class DimmSwitchChangedEvent extends Event<Double> {
    public final int device;

    @Autowired
    private DeviceService deviceService;


    public DimmSwitchChangedEvent(int deviceID){
        super(deviceID);
        this.device = deviceID;
    }

    /** Get's current value of state
     * @return the value of the state of the device
     **/
    @Override
    public Double get() {
        return ((DimmableSwitch) deviceService.get(device)).getState();
    }
}
