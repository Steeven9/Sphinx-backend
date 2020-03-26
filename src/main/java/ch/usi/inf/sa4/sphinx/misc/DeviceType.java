package ch.usi.inf.sa4.sphinx.misc;
/*
1 (=Light)
2 (=DimmableLight)
3 (=Switch)
4 (=DimmableSwitch)
5 (=StatelessDimmableSwitch)
6 (=SmartPlug)
7 (=HumiditySensor)
8 (=LightSensor)
9 (=TempSensor)
10 (=MotionSensor)
 */

import ch.usi.inf.sa4.sphinx.model.*;

/**
 *
 */
public enum DeviceType {
    INVALID_DEVICE,
    LIGHT,
    DIMMABLE_LIGHT,
    SWITCH,
    DIMMABLE_SWITCH,
    STATELESS_DIMMABLE_SWITCH,
    SMART_PLUG,
    HUMIDITY_SENSOR,
    LIGHT_SENSOR,
    TEMP_SENSOR,
    MOTION_SENSOR;


    /**
     * @param d the int representing the DeviceType according to the API
     * @return the corresponding DeviceType
     */
    public static DeviceType intToDeviceType(int d) {
        switch (d) {
            case 1:
                return LIGHT;
            case 2:
                return DIMMABLE_LIGHT;
            case 3:
                return SWITCH;
            case 4:
                return DIMMABLE_SWITCH;
            case 5:
                return STATELESS_DIMMABLE_SWITCH;
            case 6:
                return SMART_PLUG;
            case 7:
                return HUMIDITY_SENSOR;
            case 8:
                return LIGHT_SENSOR;
            case 9:
                return TEMP_SENSOR;
            case 10:
                return MOTION_SENSOR;
            default:
                return INVALID_DEVICE;
        }
    }


    /**
     * @param d a given Device
     * @return the DeviceType of the given Device
     */
    public static DeviceType deviceToDeviceType(Device d) {
        return deviceClassToDeviceType(d.getClass());
    }


    /**
     * @param c class of an Object
     * @return The corresponding DeviceType if the class if of a Device else DeviceType.INVALID_DEVICE
     */
    public static DeviceType deviceClassToDeviceType(Class c) {
        if (Light.class.equals(c)) {
            return LIGHT;
        }
        if (DimmableLight.class.equals(c)) {
            return DIMMABLE_LIGHT;
        }
        if (Switch.class.equals(c)) {
            return SWITCH;
        }
        if (DimmableSwitch.class.equals(c)) {
            return DIMMABLE_SWITCH;
        }
        if (StatelessDimmableSwitch.class.equals(c)) {
            return STATELESS_DIMMABLE_SWITCH;
        }
        if (SmartPlug.class.equals(c)) {
            return SMART_PLUG;
        }
        if (HumiditySensor.class.equals(c)) {
            return HUMIDITY_SENSOR;
        }
        if (LightSensor.class.equals(c)) {
            return LIGHT_SENSOR;
        }
        if (TempSensor.class.equals(c)) {
            return TEMP_SENSOR;
        }
        if (MotionSensor.class.equals(c)) {
            return MOTION_SENSOR;
        }
        return INVALID_DEVICE;
    }


    /**
     * @param d the DeviceType
     * @return the int corresponding to the DeviceType according to the API doc
     */
    public static int deviceTypetoInt(DeviceType d) {
        switch (d) {
            case LIGHT:
                return 1;
            case DIMMABLE_LIGHT:
                return 2;
            case SWITCH:
                return 3;
            case DIMMABLE_SWITCH:
                return 4;
            case STATELESS_DIMMABLE_SWITCH:
                return 5;
            case SMART_PLUG:
                return 6;
            case HUMIDITY_SENSOR:
                return 7;
            case LIGHT_SENSOR:
                return 8;
            case TEMP_SENSOR:
                return 9;
            case MOTION_SENSOR:
                return 10;
            default:
                return 0;
        }
    }


    /**
     * @param d the device type
     * @return a new Device according to the DeviceType
     */
    public static Device makeDevice(DeviceType d) {
        switch (d) {
            case LIGHT:
                return new Light();
            case DIMMABLE_LIGHT:
                return new DimmableLight();
            case SWITCH:
                return new Switch();
            case DIMMABLE_SWITCH:
                return new DimmableSwitch();
            case STATELESS_DIMMABLE_SWITCH:
                return new StatelessDimmableSwitch();
            case SMART_PLUG:
                return new SmartPlug();
            case HUMIDITY_SENSOR:
                return new HumiditySensor();
            case LIGHT_SENSOR:
                return new LightSensor();
            case TEMP_SENSOR:
                return new TempSensor();
            case MOTION_SENSOR:
                return new MotionSensor();
            default:
                return null;
        }
    }
}