package ch.usi.inf.sa4.sphinx.model.Coupling;

import ch.usi.inf.sa4.sphinx.misc.ServiceProvider;
import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.Observer;
import ch.usi.inf.sa4.sphinx.service.DeviceService;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
public abstract class Coupling<X extends Device, Y extends Device> extends Observer<X> {
    @ManyToOne(targetEntity = Device.class)
    private Y device2;

    public Coupling(X device1, Y device2) {
        super(device1);
        this.device2 = device2;
    }

    public X getDevice1() {
        return  getDevice();
    }

    public Y getDevice2() {
        return device2;
    }



}
