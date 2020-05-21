package ch.usi.inf.sa4.sphinx.model.Coupling;

import ch.usi.inf.sa4.sphinx.misc.ServiceProvider;
import ch.usi.inf.sa4.sphinx.model.DimmableLight;
import ch.usi.inf.sa4.sphinx.model.DimmableSwitch;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

/**
 *
 */
@Entity
@NoArgsConstructor
public class DimmSwitchToDimmLight extends Coupling<DimmableSwitch, DimmableLight> {
    public DimmSwitchToDimmLight(DimmableSwitch device1, DimmableLight device2) {
        super(device1, device2);
    }

    @Override
    public void run() {
        getAffected().setState(getObserved().getIntensity());
        ServiceProvider.getStaticDeviceService().update(getAffected());
    }

}
