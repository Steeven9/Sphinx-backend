package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.misc.DeviceType;

import javax.persistence.Entity;

/**
 * A StatelessDimmableSwitch is a dimmable switch that can change the intensity level of
 * a dimmable light, without storing the intensity level.
 */
@Entity
public class StatelessDimmableSwitch extends Device {
    private boolean button;

    /**
     * Creates an initial StatelessDimmableSwitch with state set to '-'.
     */
    public StatelessDimmableSwitch() {
        this.button = false;
    }




    /**
     * Returns true if the intensity was incremented or false if it was decremented.
     * @return true if the intensity was incremented, false otherwise
     */
    public boolean isIncrementing() {
        return button;
    }

    /**
     * Set to true if the last action of this switch was incrementing, false otherwise.
     * @param value true if was incrementing, false otherwise
     */
    public void setIncrement(boolean value) {
        this.button = value;
        triggerEffects();
    }

    /**
     * {@inheritDoc}
     */
    @Override

    public String getLabel() {
        return this.button ? "+" : "-";
    }


    @Override
    protected DeviceType getDeviceType() {
        return DeviceType.STATELESS_DIMMABLE_SWITCH;
    }
}
