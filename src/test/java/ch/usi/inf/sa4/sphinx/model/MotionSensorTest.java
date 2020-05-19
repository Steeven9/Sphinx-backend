package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.misc.DeviceType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MotionSensorTest {

    @Test
    void testMotionSensor() {
        MotionSensor motionSensor = new MotionSensor();
        assertNotNull(motionSensor);
        assertEquals(String.class, motionSensor.getLabel().getClass());
        assertEquals(DeviceType.MOTION_SENSOR, motionSensor.getDeviceType());
    }
}