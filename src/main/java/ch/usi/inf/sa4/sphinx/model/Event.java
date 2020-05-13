package ch.usi.inf.sa4.sphinx.model;


import ch.usi.inf.sa4.sphinx.misc.ImproperImplementationException;
import ch.usi.inf.sa4.sphinx.misc.ServiceProvider;
import ch.usi.inf.sa4.sphinx.service.DeviceService;


import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Event<T> extends StorableE implements Runnable{
    private int deviceId;
    @ManyToOne
    private Coupling coupling;
    @ManyToOne
    private Automation automation;


    public Event(Coupling coupling, Automation automation) {
        this.coupling = coupling;
        this.automation = automation;
    }

    @Transient
    protected transient DeviceService deviceService;

    /**
     * @deprecated
     * This constructor should not be used. It exists only for useby the JPA.
     */
    @Deprecated
    public Event() {}

    public Event(final Integer deviceId) {
        this.deviceId = deviceId;
        this.deviceService = ServiceProvider.getStaticDeviceService();
        if(deviceService == null) {
            throw new ImproperImplementationException("ServiceProvider not providing access to requested Services");
        }
    }


    public void run(){
        if(coupling != null) coupling.run();
        if(automation != null) automation.run();
    }


    public Event(final Event<T> event){
        this.deviceId = event.deviceId;
    }


    public int getDeviceId() {
        return deviceId;
    }

    public abstract T get();


}
