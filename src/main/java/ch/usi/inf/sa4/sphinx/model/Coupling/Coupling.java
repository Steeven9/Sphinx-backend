package ch.usi.inf.sa4.sphinx.model.Coupling;

import ch.usi.inf.sa4.sphinx.misc.ServiceProvider;
import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.Observer;
import ch.usi.inf.sa4.sphinx.service.DeviceService;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Coupling<X extends Device, Y extends Device> extends Observer<X> {
    @ManyToOne(targetEntity = Device.class)
    private Y device2;
    @Transient
    DeviceService deviceService;

    public Coupling(X device1, Y device2) {
        super(device1);
        this.device2 = device2;
        this.deviceService = ServiceProvider.getStaticDeviceService();
    }

    public X getDevice1() {
        return  getDevice();
    }

    public Y getDevice2() {
        return device2;
    }

}
