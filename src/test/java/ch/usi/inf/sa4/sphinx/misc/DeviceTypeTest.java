package ch.usi.inf.sa4.sphinx.misc;

import ch.usi.inf.sa4.sphinx.service.CouplingService;
import ch.usi.inf.sa4.sphinx.service.RoomService;
import org.junit.jupiter.api.DisplayName;
import ch.usi.inf.sa4.sphinx.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@SpringBootTest
class DeviceTypeTest {

    @Autowired
    RoomService roomService;
    @Autowired
    CouplingService couplingService;

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
                arguments(11, DeviceType.THERMOSTAT),
                arguments(12, DeviceType.SMART_CURTAIN),
                arguments(0, DeviceType.INVALID_DEVICE)
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

    @Test
    @DisplayName("Compare Device with its DeviceType")
    void TestDeviceClassToDeviceType() {
        assertAll(
                () -> assertEquals(DeviceType.deviceToDeviceType(new Light(roomService, couplingService)), DeviceType.LIGHT),
                () -> assertEquals(DeviceType.deviceToDeviceType(new DimmableLight(roomService, couplingService)), DeviceType.DIMMABLE_LIGHT),
                () -> assertEquals(DeviceType.deviceToDeviceType(new Switch(roomService, couplingService)), DeviceType.SWITCH),
                () -> assertEquals(DeviceType.deviceToDeviceType(new DimmableSwitch(roomService, couplingService)), DeviceType.DIMMABLE_SWITCH),
                () -> assertEquals(DeviceType.deviceToDeviceType(new StatelessDimmableSwitch(roomService, couplingService)), DeviceType.STATELESS_DIMMABLE_SWITCH),
                () -> assertEquals(DeviceType.deviceToDeviceType(new SmartPlug(roomService, couplingService)), DeviceType.SMART_PLUG),
                () -> assertEquals(DeviceType.deviceToDeviceType(new HumiditySensor(roomService, couplingService)), DeviceType.HUMIDITY_SENSOR),
                () -> assertEquals(DeviceType.deviceToDeviceType(new LightSensor(roomService, couplingService)), DeviceType.LIGHT_SENSOR),
                () -> assertEquals(DeviceType.deviceToDeviceType(new TempSensor(roomService, couplingService)), DeviceType.TEMP_SENSOR),
                () -> assertEquals(DeviceType.deviceToDeviceType(new MotionSensor(roomService, couplingService)), DeviceType.MOTION_SENSOR),
                () -> assertEquals(DeviceType.deviceToDeviceType(new Thermostat(roomService, couplingService)), DeviceType.THERMOSTAT),
                () -> assertEquals(DeviceType.deviceToDeviceType(new SmartCurtain(roomService, couplingService)), DeviceType.SMART_CURTAIN)
        );
        assertEquals(DeviceType.deviceClassToDeviceType(User.class), DeviceType.INVALID_DEVICE);
    }

    @Test
    @DisplayName("Create a Device based on given DeviceType")
    void TestMakeDevice() {
        assertAll(
                () -> assertEquals(DeviceType.makeDevice(DeviceType.LIGHT, roomService, couplingService).getClass(), Light.class),
                () -> assertEquals(DeviceType.makeDevice(DeviceType.DIMMABLE_LIGHT, roomService, couplingService).getClass(), DimmableLight.class),
                () -> assertEquals(DeviceType.makeDevice(DeviceType.SWITCH, roomService, couplingService).getClass(), Switch.class),
                () -> assertEquals(DeviceType.makeDevice(DeviceType.DIMMABLE_SWITCH, roomService, couplingService).getClass(), DimmableSwitch.class),
                () -> assertEquals(DeviceType.makeDevice(DeviceType.STATELESS_DIMMABLE_SWITCH, roomService, couplingService).getClass(), StatelessDimmableSwitch.class),
                () -> assertEquals(DeviceType.makeDevice(DeviceType.SMART_PLUG, roomService, couplingService).getClass(), SmartPlug.class),
                () -> assertEquals(DeviceType.makeDevice(DeviceType.HUMIDITY_SENSOR, roomService, couplingService).getClass(), HumiditySensor.class),
                () -> assertEquals(DeviceType.makeDevice(DeviceType.LIGHT_SENSOR, roomService, couplingService).getClass(), LightSensor.class),
                () -> assertEquals(DeviceType.makeDevice(DeviceType.TEMP_SENSOR, roomService, couplingService).getClass(), TempSensor.class),
                () -> assertEquals(DeviceType.makeDevice(DeviceType.MOTION_SENSOR, roomService, couplingService).getClass(), MotionSensor.class),
                () -> assertEquals(DeviceType.makeDevice(DeviceType.THERMOSTAT, roomService, couplingService).getClass(), Thermostat.class),
                () -> assertEquals(DeviceType.makeDevice(DeviceType.SMART_CURTAIN, roomService, couplingService).getClass(), SmartCurtain.class)
        );
        assertNull(DeviceType.makeDevice(DeviceType.INVALID_DEVICE, roomService, couplingService));
    }
}