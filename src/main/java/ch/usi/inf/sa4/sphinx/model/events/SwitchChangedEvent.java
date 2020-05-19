//package ch.usi.inf.sa4.sphinx.model.events;
//
//import ch.usi.inf.sa4.sphinx.model.Device;
//import ch.usi.inf.sa4.sphinx.model.Switch;
//import ch.usi.inf.sa4.sphinx.model.effects.Effect;
//import org.springframework.lang.NonNull;
//
//import javax.persistence.Entity;
//import javax.persistence.ManyToOne;
//import java.util.List;
//
///**
// * Event associated with the change of the on status of a Switch.
// */
//@Entity
//public class SwitchChangedEvent extends Event<Boolean> {
//
//    @ManyToOne
//    private Switch device;
//
////
////    /**
////     * Constructor.
////     *
////     * @param deviceId the id of a device
////     */
////    public SwitchChangedEvent(@NotNull final int deviceId) {
////        super(deviceId);
////    }
//
//    public SwitchChangedEvent(@NonNull final Switch device, List<Effect<Boolean>> effects) { //Double?
//        this.device = device;
//        effects.forEach(this::addEffect);
//    }
//
//
//    public SwitchChangedEvent(@NonNull final Switch device, Effect<Boolean> effect) { //Double?
//        this.device = device;
//        addEffect(effect);
//    }
//
////    private SwitchChangedEvent(final SwitchChangedEvent other) {
////        super(other);
////    }
//
//
//    @Override
//    public Switch getDevice() {
//        return device;
//    }
//
//    /**
//     * Gets current state of device
//     *
//     * @return true if the switch is on, false otherwise
//     **/
//    @Override
//    public Boolean get() {
//        return device.isOn();
//    }
//
//}
