package ch.usi.inf.sa4.sphinx.model;

public class DimmableSwitch extends Device {
    private double state;

    /* Computes whether the power is 'on' or 'off'.
     * @return a String stating whether switch is 'on' or 'off'
     */
    public String getLabel(){
     return state + "%";
    }

    public void setState(double newState) {
        state = newState;
        triggerObservers();

    }
    public double getState() {
        return state;
    }

}