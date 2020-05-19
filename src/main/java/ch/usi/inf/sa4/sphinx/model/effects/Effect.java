//package ch.usi.inf.sa4.sphinx.model.effects;
//
//
//import ch.usi.inf.sa4.sphinx.model.Device;
//import ch.usi.inf.sa4.sphinx.model.StorableE;
//
//import javax.persistence.*;
//
///**
// * This class reproduces a generic effect on a given Device
// * @param <T> The type used to execute the Effect.
// * @see Device
// */
//@Entity
//@Inheritance(strategy = InheritanceType.JOINED)
//public abstract class Effect<T> extends StorableE {
//    private int deviceId;
//
//
//    /**
//     * @deprecated
//     * This constructor should not be used. It exists only for use by the JPA.
//     */
//    @Deprecated
//    public Effect() {}
//
//    /**
//     * Constructor
//     * @param deviceId the id of the device this Effect affects
//     */
//    public Effect(final Integer deviceId) {
//        this.deviceId = deviceId;
//    }
//
//    /**
//     * Executes given an effect to apply
//     * @param effect the effect to apply
//     */
//    public abstract void execute(T effect);
//
//    public Integer getDeviceId() {
//        return deviceId;
//    }
//}
