package ch.usi.inf.sa4.sphinx.model;


/**
 * A DimmableLight is a type of light that can be dimmable, i.e. support different levels of intensity.
 */
public class DimmableLight extends Device {
    private double intensity;
    private String type;

    /**
     * Creates a special light that can be dimmable, with intensity set to maximum.
     * @param type a type of the light
     */
    public DimmableLight(String type) {
        super();
        this.intensity = 100.0;
        this.type = type;
    }

    /**
     * Sets the intensity level of this light.
     * @param intensity the intensity to set for this light
     * @throws IllegalArgumentException if the argument is bigger than 100 or smaller than 0
     */
    public void setIntensity(double intensity) throws IllegalArgumentException{
        if (intensity > 100.0 || intensity < 0.0){
            throw new IllegalArgumentException("Intensity must be between 0 and 100");
        } else {
            this.intensity = intensity;
        }
    }

    /**
     * Returns the intensity level of this light.
     * @return the intensity level of this light
     */
    public double getIntensity() {
        return intensity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabel() {
        return this.name + ", " + this.on + ", " + this.type + ", " + this.intensity;
    }
}