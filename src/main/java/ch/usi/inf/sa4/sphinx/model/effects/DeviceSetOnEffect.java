//package ch.usi.inf.sa4.sphinx.model.effects;
//
//import ch.usi.inf.sa4.sphinx.model.Device;
//import ch.usi.inf.sa4.sphinx.service.DeviceService;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import javax.persistence.Entity;
//import javax.persistence.OneToMany;
//import javax.persistence.Transient;
//
///**
// * Effect that sets the on status of a Device
// */
//@Entity
//public class DeviceSetOnEffect extends Effect<Boolean> {
//    @Autowired
//    @Transient
//    private DeviceService deviceService;
//    @OneToMany
//    Device device;
//
//
////    /**
////     * @param deviceID The Device linked to this Effect
////     */
////    public DeviceSetOnEffect(final int deviceID) {
////        super(deviceID);
////    }
//
//    public DeviceSetOnEffect(final Device device) {
//        this.device = device;
//
//    }
//
//
//    /**
//     * checks if value of lights is off e.g, false; if so it turn them on and vice versa.
//     *
//     * @param effect: the current value of the device
//     **/
////    public void execute(final Boolean effect) {
////        deviceService.get(getDeviceId()).get().setOn(effect);
////    }
//    public void execute(final Boolean effect) {
//        device.setOn(effect);
//        deviceService.update(device);
//    }
//}
