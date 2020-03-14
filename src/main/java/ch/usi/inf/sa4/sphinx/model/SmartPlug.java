package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.misc.NotImplementedException;

public class SmartPlug extends Device {
    private double energy;

    public SmartPlug() {
        super();
        this.energy = 0.0;
    }

    /**
     * Resets to 0 consumed energy by this smart-plug.
     */
    public void reset(){
        this.energy = 0.0;
    }


    @Override
    public String getLabel() {
        throw new NotImplementedException();
    }
    public double getPowerUsed() {
        throw new NotImplementedException();
    }
}