package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.misc.DeviceType;

import javax.persistence.Entity;
import java.util.Random;


/**
 * A MotionSensor is a sensor that detects a presence of a person in a certain position.
 */
@Entity
public class MotionSensor extends Device {

    /**
     * Creates a MotionSensor with initial state set to false.
     */
    public MotionSensor() {
    }


    /**
     * Checks if the person is detected.
     * @return true if the person is detected, false otherwise
     */
    public boolean isDetected() {
        Random rnd = new Random();
        triggerEffects();
        return rnd.nextBoolean();
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