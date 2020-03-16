package ch.usi.inf.sa4.sphinx.model;

/**
 * A switch is a switch that can turn on and off a device.
 */
public class Switch extends Device {

    public Switch() {
        super();
    }

    /**
     * Returns the power percentage for a given switch.
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
    }

    /**
     * Computes whether the power is 'on' or 'off'.
     * @return a String stating whether switch is 'on' or 'off'
     */
    @Override
    public String getLabel(){
        return on ? "on": "off";
    }

}