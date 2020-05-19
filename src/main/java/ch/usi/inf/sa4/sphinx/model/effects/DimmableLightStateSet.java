//package ch.usi.inf.sa4.sphinx.model.effects;
//
//
//import ch.usi.inf.sa4.sphinx.model.DimmableLight;
//import ch.usi.inf.sa4.sphinx.service.DeviceService;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import javax.persistence.Entity;
//import javax.persistence.Transient;
//
///**
// * Effect that sets a DimmableLight state
// * @see DimmableLight
// */
//@Entity
//public class DimmableLightStateSet extends Effect<Double> {
//
//    @Autowired
//    @Transient
//    private DeviceService deviceService;
//    private DimmableLight device;
//
//
////    /**
////     * Constructor.
////     *
////     * @param deviceID the id to set the DimmableLightStateSet to
////     **/
////    public DimmableLightStateSet(final Integer deviceID) {
////        super(deviceID);
////    }
//
//
//    public DimmableLightStateSet(DimmableLight device){
//        this.device = device;
//    }
//
//    /**
//     * Sets state to the effect value.
//     *
//     * @param effect: new value of the state
//     **/
//    public void execute(final Double effect) {
//        device.setState(effect);
//        deviceService.update(device);
//    }
//
//
//}
