package ch.usi.inf.sa4.sphinx.model;


import ch.usi.inf.sa4.sphinx.misc.NotImplementedException;

public class DimmableLight extends Device {
    private double intensity;

    public double getIntensity() {
        return intensity;
    }

    public String getLabel() {
        throw new NotImplementedException();
    }
}