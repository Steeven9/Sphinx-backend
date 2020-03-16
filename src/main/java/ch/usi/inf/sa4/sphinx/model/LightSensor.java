package ch.usi.inf.sa4.sphinx.model;


import java.util.Random;

/**
 * A light sensor measures the quantity og light (in lumen) in a given room.
 */
public class LightSensor extends Device {
    private double lumen;

    /**
     * Create a light sensor set to 500 lumen.
     */
    public LightSensor() {
        super();
        this.lumen = 500;
    }

    /**
     * Returns the quantity of light (in lumen) in this room.
     * @return the quantity of light
     */
    public double getLumen() {
        double var = new Random().nextDouble();
        return lumen - var - 0.5;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabel() {
        return this.name +", " + this.lumen + " lumen";
    }
}