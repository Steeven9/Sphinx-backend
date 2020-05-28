package ch.usi.inf.sa4.sphinx.model.triggers;

import ch.usi.inf.sa4.sphinx.model.Automation;
import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.MotionSensor;
import ch.usi.inf.sa4.sphinx.model.Sensor;
import ch.usi.inf.sa4.sphinx.model.conditions.MotionCondition;
import ch.usi.inf.sa4.sphinx.model.conditions.OnCondition;
import ch.usi.inf.sa4.sphinx.model.conditions.SensorQuantityCondition;
import lombok.NonNull;

/**
 * Factory for Triggers.
 */
public class TriggerFactory {
    /**
     * Generates a Trigger.
     * <p>
     * This function will produce a Trigger that will run the targeted Automation when
     * the value of the specified Device reaches the target value under the specified
     * condition type.
     * If in case of incompatibility between the Device, the type of condition, and the target an
     * IllegalArgumentException will be thrown.
     *
     * @param device     the Device whose status is to to be observed.
     * @param target     the target value of the observed Device
     * @param type       the type of condition to apply
     * @param automation the automation to run when the condition verifies.
     * @return the produced trigger.
     */
    public static Trigger makeEvent(@NonNull Device device,
                                    @NonNull Object target,
                                    @NonNull ConditionType type,
                                    @NonNull Automation automation) {

        try {
            switch (type) {
                case SENSOR_OVER:
                    return new SensorChanged((Sensor) device, automation, (Double) target, SensorQuantityCondition.Operator.GREATER);
                case SENSOR_UNDER:
                    return new SensorChanged((Sensor) device, automation, (Double) target, SensorQuantityCondition.Operator.SMALLER);
                case MOTION_DETECTED:
                    return new MotionChanged((MotionSensor) device, automation, (Boolean) target, MotionCondition.Operator.EQUAL);
                case MOTION_NOT_DETECTED:
                    return new MotionChanged((MotionSensor) device, automation, (Boolean) target, MotionCondition.Operator.NOT_EQUAL);
                case DEVICE_ON:
                    return new OnChanged(device, automation, OnCondition.Operator.ON);
                case DEVICE_OFF:
                    return new OnChanged(device, automation, OnCondition.Operator.OFF);
                default:
                    throw new IllegalArgumentException("Invalid type");
            }
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Incompatible event type/target type", e);
        }
    }
}
