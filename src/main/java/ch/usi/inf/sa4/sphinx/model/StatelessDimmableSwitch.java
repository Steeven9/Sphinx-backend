package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.service.CouplingService;
import ch.usi.inf.sa4.sphinx.service.RoomService;

/**
 * A StatelessDimmableSwitch is a dimmable switch that can change the intensity level of
 * a dimmable light, without storing the intensity level.
 */
public class StatelessDimmableSwitch extends Device {
    private boolean button;

    /**
     * Creates an initial StatelessDimmableSwitch with state set to '-'.
     */
    public StatelessDimmableSwitch(RoomService roomService, CouplingService couplingService) {
        super(roomService, couplingService);
        this.button = false;
    }

    /** Constructor.
     * @param s a StatelessDimmableSwitch
     **/
    public StatelessDimmableSwitch(StatelessDimmableSwitch s) {
        super(s);
        button = s.button;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StatelessDimmableSwitch makeCopy() {
        return new StatelessDimmableSwitch(this);
    }

    /**
     * Returns true if the intensity was incremented or false if it was decremented.
     * @return true if the intensity was incremented, false otherwise
     */
    public boolean isIncrementing() {
        return button;
    }

    /**
     * Set to true if the last action of this switch was incrementing, false otherwise.
     * @param value true if was incrementing, false otherwise
     */
    public void setIncrement(boolean value) {
        this.button = value;
        triggerEffects();
    }

    /**
     * {@inheritDoc}
     */
    @Override

    public String getLabel() {
        return this.button ? "+" : "-";
    }
}
