package ch.usi.inf.sa4.sphinx.model.Coupling;

import ch.usi.inf.sa4.sphinx.misc.ServiceProvider;
import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.Observer;
import ch.usi.inf.sa4.sphinx.service.DeviceService;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
@Inheritance
public abstract class Coupling<X extends Device, Y extends Device> extends Observer {
    @ManyToOne(targetEntity = Device.class)
    protected X device1;
    @ManyToOne(targetEntity = Device.class)
    protected Y device2;
    @Transient
    DeviceService deviceService;

    public Coupling(X device1, Y device2) {
        this.device1 = device1;
        this.device2 = device2;
        this.deviceService = ServiceProvider.getStaticDeviceService();
    }

    public X getDevice1() {
        return device1;
    }

    public Y getDevice2() {
        return device2;
    }


    public Coupling() {
    }
}
