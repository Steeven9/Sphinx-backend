package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.misc.StatusHolder;
import ch.usi.inf.sa4.sphinx.view.SerialisableDevice;
import com.google.gson.annotations.Expose;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * A dimmable is a dimmable device, that has an internal state (the intensity level).
 */
@Entity
public abstract class Dimmable extends Device implements StatusHolder<Double> {

    @Expose
    @Column
    private double intensity;

    /**
     * {@inheritDoc}
     */
    @Override
    public SerialisableDevice serialise() {
        final SerialisableDevice sd = super.serialise();
        sd.slider = this.intensity;
        return sd;
    }

    /**
     * Constructor.
     * Initialize a dimmable device, with intensity set to maximum.
     */
    protected Dimmable() {
        this.intensity = 1.0;
    }


    /**
     * Returns the intensity level of this DimmableSwitch.
     *
     * @return the intensity level of this DimmableSwitch
     */
    public double getIntensity() {
        return intensity;
    }


    @Override
    public Double getStatus() {
        return getIntensity();
    }

    /**
     * Changes the state of this DimmableSwitch and remembers it.
     * @param newState a new intensity level to be set
     * @throws IllegalArgumentException if the intensity level is more than 1.0 or less than 0.0
     */
    public void setState(final double newState) throws IllegalArgumentException {
        if (newState > 1 || newState < 0) {
            throw new IllegalArgumentException("Intensity must be between 0.0 and 1.0");
        } else {
            intensity = newState;
            triggerEffects();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabel() {
        return intensity * 100 + "%";
    }



}
