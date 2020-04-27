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
import ch.usi.inf.sa4.sphinx.service.CouplingService;
import ch.usi.inf.sa4.sphinx.service.RoomService;


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
    THERMOSTAT;


    /**
     * Given an integer, returns the device type assigned to that value.
     *
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
            case 11:
                return THERMOSTAT;
            case 12:
                return SMART_CURTAIN;
            default:
                return INVALID_DEVICE;
        }
    }


    /** Given a device, returns the DeviceType corresponding to the device's class.
     * @param d a given Device
     * @return the DeviceType of the given Device
     */
    public static DeviceType deviceToDeviceType(Device d) {
        return deviceClassToDeviceType(d.getClass());
    }


    /** Given a device Class, returns the DeviceType of that class
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
        if (SmartCurtain.class.equals(c)) {
            return SMART_CURTAIN;
        }
        if (Thermostat.class.equals(c)) {
            return THERMOSTAT;
        }
        return INVALID_DEVICE;
    }



    /** Given a DeviceType returns the integer value used to transmit the enum over the network.
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
            case THERMOSTAT:
                return 11;
            case SMART_CURTAIN:
                return 12;
            default:
                return 0;
        }
    }


    /**
     * Given a DeviceType, returns a new object of that type.
     *
     * @param d the device type
     * @return a new Device according to the DeviceType
     */
    public static Device makeDevice(DeviceType d, RoomService roomService, CouplingService couplingService) {
        switch (d) {
            case LIGHT:
                return new Light(roomService, couplingService);
            case DIMMABLE_LIGHT:
                return new DimmableLight(roomService, couplingService);
            case SWITCH:
                return new Switch(roomService, couplingService);
            case DIMMABLE_SWITCH:
                return new DimmableSwitch(roomService, couplingService);
            case STATELESS_DIMMABLE_SWITCH:
                return new StatelessDimmableSwitch(roomService, couplingService);
            case SMART_PLUG:
                return new SmartPlug(roomService, couplingService);
            case HUMIDITY_SENSOR:
                return new HumiditySensor(roomService, couplingService);
            case LIGHT_SENSOR:
                return new LightSensor(roomService, couplingService);
            case TEMP_SENSOR:
                return new TempSensor(roomService, couplingService);
            case MOTION_SENSOR:
                return new MotionSensor(roomService, couplingService);
            case THERMOSTAT:
                return new Thermostat(roomService, couplingService);
            case SMART_CURTAIN:
                return new SmartCurtain(roomService, couplingService);
            default:
                return null;
        }
    }
}
