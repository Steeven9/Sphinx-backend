package ch.usi.inf.sa4.sphinx.model;

/**
 * A smart-plug is a plug that can be added by user. It consumes some energy while in use.
 */
public class SmartPlug extends Device {
    private double powerUsed;

    /**
     * Creates a new smart-plug with 0 consumed energy.
     */
    public SmartPlug() {
        super();
        this.powerUsed = 0.0;
    }

    /** Constructor.
     * @param s a SmartPlug
     **/
    protected SmartPlug(SmartPlug s) {
        super(s);
        powerUsed = s.getPowerUsed();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SmartPlug makeCopy() {
        return new SmartPlug(this);
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
}