package ch.usi.inf.sa4.sphinx.model;


/**
 * A DimmableLight is a type of light that can be dimmable, i.e. support different levels of intensity.
 */
public class DimmableLight extends Device {
    private double intensity;

    /**
     * Creates a special light that can be dimmable, with intensity set to maximum.
     */
    public DimmableLight() {
        super();
        this.intensity = 100.0;
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
     * Returns the intensity level of this DimmableSwitch.
     * @return the intensity level of this DimmableSwitch
     */
    public double getIntensity() {
        return intensity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabel() {
        return this.getIntensity() + "%";
    }
}