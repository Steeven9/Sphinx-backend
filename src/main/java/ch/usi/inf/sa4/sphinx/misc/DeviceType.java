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

public enum DeviceType {
    INVALID_DEVICE,
    LIGHT,
    DIMMABLE_LIGHT,
    SWITCH,
    DIMMABLE_SWITCH,
    SATELESS_DIMMABLE_SWITCH,
    SMART_PLUG,
    HUMIDITY_SENSOR,
    LIGHT_SENSOR,
    TEMP_SENSOR,
    MOTION_SENSOR;


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
                return SATELESS_DIMMABLE_SWITCH;
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


    public static DeviceType deviceToDeviceType(Device d) {
        if (d instanceof Light) {
            return LIGHT;
        }
        if (d instanceof DimmableLight) {
            return DIMMABLE_LIGHT;
        }
        if (d instanceof Switch) {
            return SWITCH;
        }
        if (d instanceof DimmableSwitch) {
            return DIMMABLE_SWITCH;
        }
        if (d instanceof StatelessDimmableSwitch) {
            return SATELESS_DIMMABLE_SWITCH;
        }
        if (d instanceof SmartPlug) {
            return SMART_PLUG;
        }
        if (d instanceof HumiditySensor) {
            return HUMIDITY_SENSOR;
        }
        if (d instanceof LightSensor) {
            return LIGHT_SENSOR;
        }

        if (d instanceof TempSensor) {
            return TEMP_SENSOR;
        }
        if (d instanceof MotionSensor) {
            return MOTION_SENSOR;
        }
        return INVALID_DEVICE;
    }


    public static int deviceClassToInt(Class c) {

        if (c.isInstance(Light.class)) {
            return 1;
        }
        if (c.isInstance(DimmableLight.class)) {
            return 2;
        }
        if (c.isInstance(Switch.class)) {
            return 3;
        }
        if (c.isInstance(DimmableSwitch.class)) {
            return 4;
        }
        if (c.isInstance(StatelessDimmableSwitch.class)) {
            return 5;
        }
        if (c.isInstance(SmartPlug.class)) {
            return 6;
        }
        if (c.isInstance(HumiditySensor.class)) {
            return 7;
        }
        if (c.isInstance(LightSensor.class)) {
            return 8;
        }
        if (c.isInstance(TempSensor.class)) {
            return 9;
        }
        if (c.isInstance(MotionSensor.class)) {
            return 10;
        }
        return 0;
    }


    public static int deviceTypeint(DeviceType d) {
        return deviceClassToInt(d.getClass());
    }


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
            case SATELESS_DIMMABLE_SWITCH:
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
