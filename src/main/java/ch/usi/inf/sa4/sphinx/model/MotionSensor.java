package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.misc.NotImplementedException;

public class MotionSensor extends Device {

    private boolean detected;

    public MotionSensor() {
        super();
        this.detected = false;
    }

    public void setDetected(boolean detected) {
        this.detected = detected;
    }


    @Override
    public String getLabel() {
        throw new NotImplementedException();
    }
}