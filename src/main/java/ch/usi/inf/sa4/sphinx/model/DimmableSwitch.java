package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.misc.DeviceType;

import javax.persistence.Entity;

/**
 * A DimmableSwitch is a switch that can set and store the intensity for given device.
 */
@Entity
public class DimmableSwitch extends Dimmable {

    @Override
    public DeviceType getDeviceType() {
        return DeviceType.DIMMABLE_SWITCH;
    }

    @Override
    public void setOn(boolean on) {
        if (on != isOn()) {
            super.setOn(on);
            triggerEffects();
        }
    }
}