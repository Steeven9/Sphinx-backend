//package ch.usi.inf.sa4.sphinx.model.effects;
//
//import ch.usi.inf.sa4.sphinx.model.Device;
//import ch.usi.inf.sa4.sphinx.model.DimmableLight;
//import ch.usi.inf.sa4.sphinx.service.DeviceService;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import javax.persistence.Entity;
//import javax.persistence.OneToMany;
//import javax.persistence.Transient;
//
//@Entity
//public class DimmableLightStateInc extends Effect<Double> {
//    @Autowired
//    @Transient
//    DeviceService deviceService;
//    @OneToMany
//    DimmableLight device;
//
////    /**
////     * Constructor.
////     *
////     * @param deviceID the id to set the DimmableLightStateInc to
////     **/
////    public DimmableLightStateInc(final Integer deviceID) {
////        super(deviceID);
////    }
//
//
//    public DimmableLightStateInc(final DimmableLight device){
//        this.device = device;
//    }
//
//
//
//    /**
//     * Sets current state of the Device to the given value.
//     *
//     * @param value: the new state value
//     **/
//    @Override
//    public void execute(final Double value) {
//        device.setState(device.getIntensity() + value);
//        deviceService.update(device);
//    }
//
//}
