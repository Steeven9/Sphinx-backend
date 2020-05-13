package ch.usi.inf.sa4.sphinx.model.conditions;

import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.MotionSensor;
import ch.usi.inf.sa4.sphinx.model.Sensor;
import ch.usi.inf.sa4.sphinx.model.events.EventType;
import lombok.NonNull;

public class ConditionFactory {


    public static Condition make(@NonNull Device device,
                                 @NonNull Object target,
                                 @NonNull EventType type
    ) {

        try {
            switch (type) {
                case SENSOR_OVER:
                    return new SensorQuantityCondition((Sensor) device, (Double) target, SensorQuantityCondition.Operator.GREATER);
                case SENSOR_UNDER:
                    return new SensorQuantityCondition((Sensor) device, (Double) target, SensorQuantityCondition.Operator.SMALLER);
                case MOTION_DETECTED:
                    return new MotionCondition((MotionSensor) device, (Boolean) target, MotionCondition.Operator.EQUAL);
                case MOTION_NOT_DETECTED:
                    return new MotionCondition((MotionSensor) device, (Boolean) target, MotionCondition.Operator.NOT_EQUAL);
                case DEVICE_ON:
                    return new OnCondition(device, (Boolean) target, OnCondition.Operator.ON);
                case DEVICE_OFF:
                    return new OnCondition(device, (Boolean) target, OnCondition.Operator.OFF);
                default:
                    throw new IllegalArgumentException("Invalid type");
            }
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Incompatible event type/target type");
        }
    }


}

