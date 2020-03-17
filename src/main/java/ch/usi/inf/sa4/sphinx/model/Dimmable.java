package ch.usi.inf.sa4.sphinx.model;

/**
 * A dimmable is a dimmable device, that has an internal state (the intensity level).
 */
public class Dimmable extends Device{
    private double intensity;

    /**
     * Initialize a dimmable device, with intensity set to maximum.
     */
    protected Dimmable() {
        super();
        this.intensity = 100.0;
    }

    /**
     * Returns the intensity level of this DimmableSwitch.
     * @return the intensity level of this DimmableSwitch
     */
    public double getState() {
        return intensity;
    }

    /**
     * Changes the state of this DimmableSwitch and remembers it.
     * @param newState a new intensity level to be set
     * @throws IllegalArgumentException if the intensity level is more than 100 or less than 0
     */
    public void setState(double newState) throws IllegalArgumentException{
        if (newState > 100 || newState < 0){
            throw new IllegalArgumentException("Intensity must be between 0 and 100");
        } else {
            intensity = newState;
            triggerObservers();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabel() {
        return this.getState() + "%";
    }
}
