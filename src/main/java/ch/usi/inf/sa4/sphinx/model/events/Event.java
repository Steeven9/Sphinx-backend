//package ch.usi.inf.sa4.sphinx.model.events;
//
//
//import ch.usi.inf.sa4.sphinx.misc.NotImplementedException;
//import ch.usi.inf.sa4.sphinx.model.Device;
//import ch.usi.inf.sa4.sphinx.model.StorableE;
//import ch.usi.inf.sa4.sphinx.model.effects.Effect;
//import ch.usi.inf.sa4.sphinx.view.SerialisableCondition;
//
//import javax.persistence.*;
//import java.util.ArrayList;
//import java.util.List;
//
//@Entity
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//public abstract class Event<T> extends StorableE implements Runnable {
////    private int deviceId;
////    @ManyToOne
////    private Coupling coupling;
////    @ManyToOne
////    private Automation automation;
////    @Transient
////    protected transient DeviceService deviceService;
////    @ManyToOne
////    protected Device device;
//    @OneToMany
//    protected List<Effect<T>> effects;
//
//
////    public Event(Coupling coupling, Automation automation) {
////        this.coupling = coupling;
////        this.automation = automation;
////    }
//
////
////    public Event(Device device) {
////        this.device = device;
////    }
//
//
////    public Event(Coupling coupling) {
////        this.coupling = coupling;
////    }
////
////    /**
////     * @deprecated This constructor should not be used. It exists only for useby the JPA.
////     */
////    @Deprecated
//    public Event() {
//        this.effects =new ArrayList<>();
//    }
////
////    public Event(final Integer deviceId) {
////        this.deviceId = deviceId;
////        this.deviceService = ServiceProvider.getStaticDeviceService();
////        if (deviceService == null) {
////            throw new ImproperImplementationException("ServiceProvider not providing access to requested Services");
////        }
////    }
//
//
////    public void run() {
////        if (coupling != null) coupling.run();
////        if (automation != null) automation.run();
////    }
//
//    public void run(){
//        effects.forEach(effect->effect.execute(get()));
//    }
//
//
////    public Event(final Event<T> event) {
////        this.deviceId = event.deviceId;
////    }
//
//
////    public int getDeviceId() {
////        return deviceId;
////    }
//
//
//    public abstract Device getDevice();
//
//    public List<Effect<T>> getEffects() {
//        return effects;
//    }
//
//
//    public void addEffect(Effect<T> effect){
//        effects.add(effect);
//    }
//
//
//    public abstract T get();
//
//
//
//    public SerialisableCondition serialise() {
//        throw new NotImplementedException();
//    }
//
//
//    @Override
//    public boolean equals(Object obj) {
//        try{
//            return ((Event) obj).id.equals(id);
//        } catch (ClassCastException e){
//            return false;
//        }
//    }
//}
