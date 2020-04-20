package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.misc.DeviceType;

import javax.persistence.Entity;

/**
 * A smart-plug is a plug that can be added by user. It consumes some energy while in use.
 */
@Entity
public class SmartPlug extends Device {
    private double powerUsed;

    /**
     * Creates a new smart-plug with 0 consumed energy.
     */
    public SmartPlug() {
        super();
        this.powerUsed = 0.0;
    }



    /**
     * Returns the consumed energy by this smart-plug.
     * @return the consumed energy
     */
    public double getPowerUsed() {
        return powerUsed += 10;
    }

    /**
     * Resets to 0 consumed energy by this smart-plug.
     */
    public void reset(){
        this.powerUsed = 0.0;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabel() {
        return this.getPowerUsed() + " kWh";
    }


    @Override
    protected DeviceType getDeviceType() {
        return DeviceType.SMART_PLUG;
    }
}