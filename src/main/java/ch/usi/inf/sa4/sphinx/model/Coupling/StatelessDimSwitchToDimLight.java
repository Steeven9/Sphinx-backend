package ch.usi.inf.sa4.sphinx.model.Coupling;

import ch.usi.inf.sa4.sphinx.misc.ServiceProvider;
import ch.usi.inf.sa4.sphinx.model.DimmableLight;
import ch.usi.inf.sa4.sphinx.model.StatelessDimmableSwitch;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor
public class StatelessDimSwitchToDimLight extends Coupling<StatelessDimmableSwitch, DimmableLight> {
    private double increment;

    public StatelessDimSwitchToDimLight(StatelessDimmableSwitch device1, DimmableLight device2) {
        super(device1, device2);
        increment = 0.1;
    }

    @Override
    public void run() {
        double newState = getDevice2().getIntensity() + (getDevice1().isIncrementing() ? increment : -increment);
        if (newState > 1) newState = 1;
        if (newState < 0) newState = 0;
        getDevice2().setState(newState);
        ServiceProvider.getStaticDeviceService().update(getDevice2());
    }

}
