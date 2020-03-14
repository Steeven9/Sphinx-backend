package ch.usi.inf.sa4.sphinx.model;


import ch.usi.inf.sa4.sphinx.misc.NotImplementedException;

public class DimmableLight extends Device {
    private double intensity;

    public DimmableLight() {
        super();
        this.intensity = 0.0;
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

    public String getLabel() {
        throw new NotImplementedException();
    }
}