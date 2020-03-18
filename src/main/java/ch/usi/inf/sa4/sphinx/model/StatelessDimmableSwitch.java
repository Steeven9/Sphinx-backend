package ch.usi.inf.sa4.sphinx.model;

/**
 * A StatelessDimmableSwitch is a dimmable switch that can change the intensity level of
 * a dimmable light, without storing the intensity level.
 */
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
        triggerObservers();
    }

    /** Computes whether the percentage of power used.
     * @return a String stating the percentage of power in use
     **/
    public String getLabel() {
        return this.button ? "+" : "-";
    }
}
