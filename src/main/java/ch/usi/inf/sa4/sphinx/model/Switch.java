package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.misc.DeviceType;
import ch.usi.inf.sa4.sphinx.misc.StatusHolder;

import javax.persistence.Entity;


/**
 * A switch is a switch that can turn on and off a device.
 */
@Entity
public class Switch extends Device implements StatusHolder<Boolean> {

    /**
     * Creates a switch, which is on.
     */
    public Switch() {

    }



    /**
     * Returns true if and only if the switch is powered.
     * @return true if the switch is on, otherwise false
     */
    public boolean getState(){
        return on;
    }

    @Override
    public Boolean getStatus() {
        return getState();
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
     * Computes whether the power is 'on' or 'off'.
     * @return a String stating whether switch is 'on' or 'off'
     */
    @Override
    public String getLabel(){
        return this.on ? "on" : "off";
    }


    @Override
    public void setOn(boolean on) {
        super.setOn(on);
        triggerEffects();
    }

    @Override
    protected DeviceType getDeviceType() {
        return DeviceType.SWITCH;
    }
}