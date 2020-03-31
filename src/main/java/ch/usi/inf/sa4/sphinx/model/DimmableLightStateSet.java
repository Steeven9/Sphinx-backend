package ch.usi.inf.sa4.sphinx.model;


import ch.usi.inf.sa4.sphinx.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;

public class DimmableLightStateSet extends Effect<Double> {
    public final int device;

    @Autowired
    private DeviceService deviceService;

    /**
     * Constructor.
     * @param deviceID the id to set the DimmableLightStateSet to
     * **/
    public DimmableLightStateSet(int deviceID){
        super(deviceID);
        this.device = deviceID;
    }
    /** Sets state to the effect value.
     * @param effect:  new value of the state
     **/
    public void execute(Double effect){
        DimmableLight dl =  ((DimmableLight) deviceService.get(device));
        dl.setState(effect);
        deviceService.update(dl);
    }
}
