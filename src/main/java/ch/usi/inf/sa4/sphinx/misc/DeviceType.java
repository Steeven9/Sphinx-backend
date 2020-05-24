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
11 (=Thermostat)
12 (=SmartCurtain)
 */

import ch.usi.inf.sa4.sphinx.model.*;


/**
 * Represent thee different types of Device.
 * INVALID_DEVICE = 0
 * LIGHT = 1
 * DIMMABLE_LIGHT =2
 * SWITCH= 3
 * DIMMABLE_SWITCH=4
 * STATELESS_DIMMABLE_SWITCH = 5
 * SMART_PLUG = 6
 * HUMIDITY_SENSOR =7
 * LIGHT_SENSOR =8
 * TEMP_SENSOR =9
 * MOTION_SENSOR= 10
 * THERMOSTAT=11
 * SMART_CURTAIN =12
 * SECURITY_CAMERA=13
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
    MOTION_SENSOR,
    SMART_CURTAIN,
    SECURITY_CAMERA,
    THERMOSTAT;


    /**
     * Given an integer, returns the device type assigned to that value.
     *
     * @param deviceType the int representing the DeviceType according to the API
     * @return the corresponding DeviceType
     */
    public static DeviceType intToDeviceType(final int deviceType) {
        switch (deviceType) {
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
            case 11:
                return THERMOSTAT;
            case 12:
                return SMART_CURTAIN;
            case 13:
                return SECURITY_CAMERA;
            default:
                return INVALID_DEVICE;
        }
    }


    /**
     * Given a device, returns the DeviceType corresponding to the device's class.
     *
     * @param device a given Device
     * @return the DeviceType of the given Device
     */
    public static DeviceType deviceToDeviceType(final Device device) {
        return deviceClassToDeviceType(device.getClass());
    }


    /**
     * Given a device Class, returns the DeviceType of that class
     *
     * @param clazz class of an Object
     * @return The corresponding DeviceType if the class if of a Device else DeviceType.INVALID_DEVICE
     */
    public static DeviceType deviceClassToDeviceType(final Class<? extends Device> clazz) {
        if (Light.class.equals(clazz)) {
            return LIGHT;
        }
        if (DimmableLight.class.equals(clazz)) {
            return DIMMABLE_LIGHT;
        }
        if (Switch.class.equals(clazz)) {
            return SWITCH;
        }
        if (DimmableSwitch.class.equals(clazz)) {
            return DIMMABLE_SWITCH;
        }
        if (StatelessDimmableSwitch.class.equals(clazz)) {
            return STATELESS_DIMMABLE_SWITCH;
        }
        if (SmartPlug.class.equals(clazz)) {
            return SMART_PLUG;
        }
        if (HumiditySensor.class.equals(clazz)) {
            return HUMIDITY_SENSOR;
        }
        if (LightSensor.class.equals(clazz)) {
            return LIGHT_SENSOR;
        }
        if (TempSensor.class.equals(clazz)) {
            return TEMP_SENSOR;
        }
        if (MotionSensor.class.equals(clazz)) {
            return MOTION_SENSOR;
        }
        if (SmartCurtain.class.equals(clazz)) {
            return SMART_CURTAIN;
        }
        if (SecurityCamera.class.equals(clazz)) {
            return SECURITY_CAMERA;
        }
        if (Thermostat.class.equals(clazz)) {
            return THERMOSTAT;
        }
        return INVALID_DEVICE;
    }


    /**
     * Given a DeviceType returns the integer value used to transmit the enum over the network.
     *
     * @param type the DeviceType
     * @return the int corresponding to the DeviceType according to the API doc
     */
    public static int deviceTypetoInt(final DeviceType type) {
        switch (type) {
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
            case THERMOSTAT:
                return 11;
            case SMART_CURTAIN:
                return 12;
            case SECURITY_CAMERA:
                return 13;
            default:
                return 0;
        }
    }


    /**
     * Given a DeviceType, returns a new object of that type.
     *
     * @param type the device type
     * @return a new Device according to the DeviceType
     */
    public static Device makeDevice(final DeviceType type) {
        switch (type) {
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
            case SMART_CURTAIN:
                return new SmartCurtain();
            case SECURITY_CAMERA:
                return new SecurityCamera();
            case THERMOSTAT:
                return new Thermostat();
            default:
                return null;
        }
    }
}
