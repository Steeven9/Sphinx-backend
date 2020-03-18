package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.controller.Storage;

public class StatelessDimmableSwitch extends Device {
    private double state;

    /** Computes whether the percentage of power used.
     * @return a String stating the percentage of power in use
     **/
    public String getLabel() {
        return Math.round(state*100) + "%";
    }




    /** Setter for the state of the device.
     * @param  newState: the new value of the device's state
     **/
    public void setState(double newState) {
        state = newState;
        triggerObservers();

    }

    /** Getter method for the state of the device.
     * @return double corresponding to the state.
     **/
    public double getState() {
        return state;
    }
}
