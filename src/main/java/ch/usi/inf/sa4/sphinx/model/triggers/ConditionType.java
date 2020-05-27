package ch.usi.inf.sa4.sphinx.model.triggers;


//        Trigger/Condition types:
//        1 (=DEVICE_ON)
//        2 (=DEVICE_OFF)
//        3 (=MOTION_DETECTED)
//        4 (=MOTION_NOT_DETECTED)
//        5 (=SENSOR_OVER when a sensor measures over a certain target)
//        6 (=SENSOR_UNDER)

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


    public  int toInt() {
        switch (this) {
            case DEVICE_OFF:
                return 1;
            case DEVICE_ON:
                return 2;
            case MOTION_DETECTED:
                return 3;
            case MOTION_NOT_DETECTED:
                return 4;
            case SENSOR_OVER:
                return 5;
            case SENSOR_UNDER:
                return 6;
        }
        return 0;
    }

    public ConditionType intToType(int a) {
        switch (a) {
            case 1:
                return ConditionType.DEVICE_ON;
            case 2:
                return ConditionType.DEVICE_OFF;
            case 3:
                return ConditionType.MOTION_DETECTED;
            case 4:
                return ConditionType.MOTION_NOT_DETECTED;
            case 5:
                return ConditionType.SENSOR_OVER;
            case 6:
                return ConditionType.SENSOR_UNDER;
            default:
                throw new IllegalArgumentException("");
        }
    }
}
