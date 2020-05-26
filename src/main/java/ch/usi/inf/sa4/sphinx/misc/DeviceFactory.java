package ch.usi.inf.sa4.sphinx.misc;

import ch.usi.inf.sa4.sphinx.model.*;

public class DeviceFactory {
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
