package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.view.SerialisableDevice;

/**
 * A dimmable is a dimmable device, that has an internal state (the intensity level).
 */
public class Dimmable extends Device {
    private double intensity;

    @Override
    public SerialisableDevice serialize(){
        SerialisableDevice sd = super.serialize();
        sd.intensity = this.intensity;
        return sd;
    }

    /**
     * Initialize a dimmable device, with intensity set to maximum.
     */
    protected Dimmable() {
        super();
        this.intensity = 1.0;
    }

    /**
     * Returns the intensity level of this DimmableSwitch.
     *
     * @return the intensity level of this DimmableSwitch
     */
    public double getState() {
        return intensity;
    }

    /**
     * Changes the state of this DimmableSwitch and remembers it.
     *
     * @param newState a new intensity level to be set
     * @throws IllegalArgumentException if the intensity level is more than 1.0 or less than 0.0
     */
    public void setState(double newState) throws IllegalArgumentException {
        if (newState > 1 || newState < 0) {
            throw new IllegalArgumentException("Intensity must be between 0.0 and 1.0");
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
        return this.getState() * 100 + "%";
    }
}
