package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.misc.DeviceType;

import javax.persistence.Entity;


/**
 * A MotionSensor is a sensor that detects a presence of a person in a certain position.
 */
@Entity
public class MotionSensor extends Device {
    private boolean detected;

    /**
     * Creates a MotionSensor with initial state set to false.
     */
    public MotionSensor() {
        this.detected = false;
    }



    /**
     * Changes state to true.
     */
    public void detected() {
        this.detected = true;
    }

    /**
     * Changes state to false.
     */
    public void notDetected() {
        this.detected = false;
    }

    /**
     * Checks if the person is detected.
     * @return true if the person is detected, false otherwise
     */
    public boolean isDetected() {
        return this.detected;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabel() {
        return "" + this.isDetected();
    }


    @Override
    protected DeviceType getDeviceType() {
        return DeviceType.MOTION_SENSOR;
    }
}