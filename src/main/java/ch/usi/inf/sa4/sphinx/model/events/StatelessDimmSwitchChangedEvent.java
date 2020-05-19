//package ch.usi.inf.sa4.sphinx.model.events;
//
//import ch.usi.inf.sa4.sphinx.model.StatelessDimmableSwitch;
//import ch.usi.inf.sa4.sphinx.model.effects.Effect;
//import org.springframework.lang.NonNull;
//
//import javax.persistence.Entity;
//import java.util.List;
//
//
///**
// * An event linked to the action of a StatelessDimmableSwitch
// *
// * @see StatelessDimmableSwitch
// */
//@Entity
//public class StatelessDimmSwitchChangedEvent extends Event<Double> {
//    private double increment;
//    private StatelessDimmableSwitch device;
//
////    /**
////     * Constructor.
////     *
////     * @param deviceID  the id of a device
////     * @param increment value for incrementing
////     **/
////    public StatelessDimmSwitchChangedEvent(@NotNull final Integer deviceID, final double increment) {
////        this.increment = increment;
////    }
//
//
//    public StatelessDimmSwitchChangedEvent(@NonNull final StatelessDimmableSwitch device,
//                                           List<Effect<Double>> effects,
//                                           final double increment) {
//        effects.forEach(this::addEffect);
//        this.device = device;
//        this.increment = increment;
//    }
//
//
//    public StatelessDimmSwitchChangedEvent(@NonNull final StatelessDimmableSwitch device,
//                                           Effect<Double> effect,
//                                           final double increment) {
//        addEffect(effect);
//        this.device = device;
//        this.increment = increment;
//    }
//
//    /**
//     * Gets current state of device
//     *
//     * @return value of state of device
//     **/
//    @Override
//    public Double get() {
//        return device.isIncrementing() ? increment : -increment;
//    }
//
//    @Override
//    public StatelessDimmableSwitch getDevice() {
//        return device;
//    }
//}
