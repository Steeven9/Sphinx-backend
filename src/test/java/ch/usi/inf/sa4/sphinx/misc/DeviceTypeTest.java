package ch.usi.inf.sa4.sphinx.misc;

import org.junit.jupiter.api.DisplayName;
import ch.usi.inf.sa4.sphinx.model.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class DeviceTypeTest {

    static Stream<Arguments> valueIntegerAndDeviceTypeProvider() {
        return Stream.of(
                arguments(1, DeviceType.LIGHT),
                arguments(2, DeviceType.DIMMABLE_LIGHT),
                arguments(3, DeviceType.SWITCH),
                arguments(4, DeviceType.DIMMABLE_SWITCH),
                arguments(5, DeviceType.STATELESS_DIMMABLE_SWITCH),
                arguments(6, DeviceType.SMART_PLUG),
                arguments(7, DeviceType.HUMIDITY_SENSOR),
                arguments(8, DeviceType.LIGHT_SENSOR),
                arguments(9, DeviceType.TEMP_SENSOR),
                arguments(10, DeviceType.MOTION_SENSOR),
                arguments(12, DeviceType.SMART_CURTAIN),
                arguments(0, DeviceType.INVALID_DEVICE)
        );
    }

    static Stream<Arguments> valueDeviceAndDeviceTypeProvider() {
        return Stream.of(
                arguments(new Light(), DeviceType.LIGHT),
                arguments(new DimmableLight(), DeviceType.DIMMABLE_LIGHT),
                arguments(new Switch(), DeviceType.SWITCH),
                arguments(new DimmableSwitch(), DeviceType.DIMMABLE_SWITCH),
                arguments(new StatelessDimmableSwitch(), DeviceType.STATELESS_DIMMABLE_SWITCH),
                arguments(new SmartPlug(), DeviceType.SMART_PLUG),
                arguments(new HumiditySensor(), DeviceType.HUMIDITY_SENSOR),
                arguments(new LightSensor(), DeviceType.LIGHT_SENSOR),
                arguments(new TempSensor(), DeviceType.TEMP_SENSOR),
                arguments(new MotionSensor(), DeviceType.MOTION_SENSOR),
                arguments(new SmartCurtain(), DeviceType.SMART_CURTAIN),
                arguments(new Thermostat(), DeviceType.THERMOSTAT)
        );
    }

    @ParameterizedTest(name = "Integer {0} is associated to DeviceType {1}")
    @MethodSource("valueIntegerAndDeviceTypeProvider")
    @DisplayName("Given a Integer, return DeviceType associated to it")
    void testIntToDeviceType(Integer device, DeviceType type) {
        assertEquals(DeviceType.intToDeviceType(device), type);
    }

    @ParameterizedTest(name = "DeviceType {1} is associated to Integer {0}")
    @MethodSource("valueIntegerAndDeviceTypeProvider")
    @DisplayName("Given a DeviceType, return Integer associated to it")
    void testDeviceTypetoInt(Integer device, DeviceType type) {
        assertEquals(DeviceType.deviceTypetoInt(type), device);
    }

    @ParameterizedTest(name = "Device {0} is associated to DeviceType {1}")
    @MethodSource("valueDeviceAndDeviceTypeProvider")
    @DisplayName("Compare Device with its DeviceType")
    void TestDeviceClassToDeviceType(Device device, DeviceType type) {
        assertEquals(DeviceType.deviceToDeviceType(device), type);
        assertEquals(DeviceType.deviceClassToDeviceType(User.class), DeviceType.INVALID_DEVICE);
    }

    @ParameterizedTest
    @MethodSource("valueDeviceAndDeviceTypeProvider")
    @DisplayName("Create a Device based on given DeviceType")
    void TestMakeDevice(Device device, DeviceType type) {
        assertEquals(DeviceType.makeDevice(type).getClass(), device.getClass());
        assertNull(DeviceType.makeDevice(DeviceType.INVALID_DEVICE));
    }
}