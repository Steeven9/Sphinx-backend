package ch.usi.inf.sa4.sphinx.model;

/**
 * A switch is a switch that can turn on and off a device.
 */
public class Switch extends Device {

    /**
     * Creates a switch, which is on.
     */
    public Switch() {
        super();
    }

    /**
     * Returns true if and only if the switch is powered.
     * @return true if the switch is on, otherwise false
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
        triggerObservers();
    }

    /**
     * Computes whether the power is 'on' or 'off'.
     * @return a String stating whether switch is 'on' or 'off'
     */
    @Override
    public String getLabel(){
        return this.on ? "on" : "off";
    }



}