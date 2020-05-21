package ch.usi.inf.sa4.sphinx.model.triggers;

/**
 * Represent the various types of conditions available.
 */
public enum ConditionType {
    SENSOR_OVER,
    SENSOR_UNDER,
    MOTION_DETECTED,
    MOTION_NOT_DETECTED,
    DEVICE_ON,
    DEVICE_OFF;
}
