package ch.usi.inf.sa4.sphinx.model;

/**
 * A DimmableSwitch is a switch that can set and store the intensity for given device.
 */
public class DimmableSwitch extends Device {
    private double state;


    public DimmableSwitch() {
        super();
        this.state = 100.0;
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
            state = newState;
            triggerObservers();
        }
    }

    /**
     * Returns the intensity level of this DimmableSwitch.
     * @return the intensity level of this DimmableSwitch
     */
    public double getState() {
        return state;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabel() {
        return this.name + ", " + this.on + Math.round(state*100) + "%";
    }
}