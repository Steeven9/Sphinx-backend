package ch.usi.inf.sa4.sphinx.model.triggers;

import ch.usi.inf.sa4.sphinx.model.*;
import ch.usi.inf.sa4.sphinx.model.conditions.MotionCondition;
import ch.usi.inf.sa4.sphinx.model.conditions.OnCondition;
import ch.usi.inf.sa4.sphinx.model.conditions.SensorQuantityCondition;
import lombok.NonNull;

public class TriggerFactory {
    public static Trigger makeEvent(@NonNull Device device,
                                  @NonNull Object target,
                                  @NonNull EventType type,
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
            throw new IllegalArgumentException("Incompatible event type/target type");
        }
    }


//    public static Event makeEvent(@NonNull Device device,
//                                  @NonNull Boolean target,
//                                  @NonNull EventType type,
//                                  @NonNull Automation automation) {
//        try {
//            switch (type) {
//                case MOTION_DETECTED:
//                    return new MotionChanged((MotionSensor) device, automation, target, MotionCondition.Operator.EQUAL);
//                case MOTION_NOT_DETECTED:
//                    return new MotionChanged((MotionSensor) device, automation, target, MotionCondition.Operator.NOT_EQUAL);
//                case DEVICE_ON:
//                    return new OnChanged(device, automation, target, OnCondition.Operator.ON);
//                case DEVICE_OFF:
//                    return new OnChanged(device, automation, target, OnCondition.Operator.OFF);
//                default:
//                    throw new IllegalArgumentException("Invalid type");
//            }
//        } catch (ClassCastException e) {
//            throw new IllegalArgumentException("Incompatible event type/target type");
//        }
//    }
//
//
//    public static Event makeEvent(@NonNull Device device,
//                                  @NonNull Double target,
//                                  @NonNull EventType type,
//                                  @NonNull Automation automation) {
//        try {
//            switch (type) {
//                case SENSOR_OVER:
//                    return new SensorChanged((Sensor) device, automation, (Double) target, SensorQuantityCondition.Operator.GREATER);
//                case SENSOR_UNDER:
//                    return new SensorChanged((Sensor) device, automation, (Double) target, SensorQuantityCondition.Operator.SMALLER);
//                default:
//                    throw new IllegalArgumentException("Invalid type");
//            }
//        } catch (ClassCastException e) {
//            throw new IllegalArgumentException("Incompatible event type/target type");
//        }
//    }


}
