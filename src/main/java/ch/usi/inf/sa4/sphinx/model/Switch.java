package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.misc.DeviceType;

import javax.persistence.Entity;

/**
 * A switch is a switch that can turn active and off a device.
 */
@Entity
public class Switch extends Device {

    /**
     * Creates a switch, which is active.
     */
    public Switch() {
        super();
    }



    /**
     * Returns true if and only if the switch is powered.
     * @return true if the switch is active, otherwise false
     */
    public boolean getState(){
        return on;
    }

    /**
     * Change the state of the switch, if true become false
     * and vice versa.
     */
    public void click(){
        this.on = !this.on;
        triggerEffects();
    }

    /**
     * Computes whether the power is 'active' or 'off'.
     * @return a String stating whether switch is 'active' or 'off'
     */
    @Override
    public String getLabel(){
        return this.on ? "active" : "off";
    }


    @Override
    protected DeviceType getDeviceType() {
        return DeviceType.SWITCH;
    }
}