package ch.usi.inf.sa4.sphinx.model;


import ch.usi.inf.sa4.sphinx.misc.ImproperImplementationException;
import ch.usi.inf.sa4.sphinx.misc.ServiceProvider;
import ch.usi.inf.sa4.sphinx.service.DeviceService;


import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Transient;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Event<T> extends StorableE {
    private int deviceId;


    /**
     * @deprecated
     * This constructor should not be used. It exists only for useby the JPA.
     */
    @Deprecated
    public Event() {}

    public Event(final Integer deviceId) {
        this.deviceId = deviceId;
    }


    public Event(final Event<T> event){
        this.deviceId = event.deviceId;
    }


    public int getDeviceId() {
        return deviceId;
    }

    public abstract T get();


}
