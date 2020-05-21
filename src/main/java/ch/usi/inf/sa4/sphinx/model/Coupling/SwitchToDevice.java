package ch.usi.inf.sa4.sphinx.model.Coupling;

import ch.usi.inf.sa4.sphinx.misc.ServiceProvider;
import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.Switch;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor
public class SwitchToDevice extends Coupling<Switch, Device> {

    public SwitchToDevice(Switch device1, Device device2) {
        super(device1, device2);
    }

    @Override
    public void run() {
        getDevice2().setOn(getDevice1().isOn());
        ServiceProvider.getStaticDeviceService().update(getDevice2());
    }

}