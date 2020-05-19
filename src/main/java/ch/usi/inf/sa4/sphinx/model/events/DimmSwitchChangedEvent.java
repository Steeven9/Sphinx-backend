//package ch.usi.inf.sa4.sphinx.model.events;
//
//
//import ch.usi.inf.sa4.sphinx.model.Device;
//import ch.usi.inf.sa4.sphinx.model.DimmableSwitch;
//import ch.usi.inf.sa4.sphinx.model.effects.Effect;
//import org.springframework.lang.NonNull;
//
//import javax.persistence.Entity;
//import javax.persistence.ManyToOne;
//import java.util.List;
//
///**
// * Event linked to the change of intensity of a DimmableSwitch
// *
// * @see DimmableSwitch
// */
//@Entity
//public class DimmSwitchChangedEvent extends Event<Double> {
//
//    @ManyToOne
//    private DimmableSwitch device;
//
//
////
////    /**
////     * Constructor.
////     *
////     * @param deviceID the id to set the DimmSwitchChangedEvent to
////     **/
////    public DimmSwitchChangedEvent(@NonNull final int deviceID) {
////        super(deviceID);
////    }
//
//
//    public DimmSwitchChangedEvent(@NonNull final DimmableSwitch device, Effect<Double> effect) {
//        this.device = device;
//        addEffect(effect);
//    }
//
//
//    public DimmSwitchChangedEvent(@NonNull final DimmableSwitch device, List<Effect<Double>> effects) {
//        this.device = device;
//        effects.forEach(this::addEffect);
//    }
//
//    @Override
//    public DimmableSwitch getDevice() {
//        return device;
//    }
//
//    /**
//     * Get's current value of state
//     *
//     * @return the value of the state of the device
//     **/
//    @Override
//    public Double get() {
//        return device.getIntensity();
//    }
//
//}
