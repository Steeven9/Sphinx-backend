package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.misc.DeviceType;
import ch.usi.inf.sa4.sphinx.view.SerialisableDevice;
import org.springframework.data.annotation.Transient;

import javax.persistence.Entity;
import java.util.Random;


/**
 * A MotionSensor is a sensor that detects a presence of a person in a certain position.
 */
@Entity
public class MotionSensor extends Device implements Generated {
    @Transient
    private final Random rnd = new Random();
    private double triggerProbability = 0.5;
    private boolean detected;

    /**
     * Checks if the person is detected.
     * @return true if the person is detected, false otherwise
     */
    public boolean isDetected() {
        return detected;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabel() {
        return "" + this.isDetected();
    }

    @Override
    public void setPropertiesFrom(final SerialisableDevice device) {
        super.setPropertiesFrom(device);
        if (device.getQuantity() != null ) triggerProbability = device.getQuantity() / 100;
    }

    @Override
    public SerialisableDevice serialise() {
        final SerialisableDevice sd = super.serialise();
        sd.setQuantity(triggerProbability * 100);
        return sd;
    }

    /**
     * Sets the physical quantity in given room with a random error set by user.
     */
    @Override
    public void generateValue() {
        this.detected = rnd.nextDouble() < triggerProbability;
        triggerEffects();
    }

    @Override
    public DeviceType getDeviceType() {
        return DeviceType.MOTION_SENSOR;
    }
}