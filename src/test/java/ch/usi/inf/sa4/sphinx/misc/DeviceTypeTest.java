package ch.usi.inf.sa4.sphinx.misc;

import ch.usi.inf.sa4.sphinx.service.CouplingService;
import ch.usi.inf.sa4.sphinx.service.RoomService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import ch.usi.inf.sa4.sphinx.model.*;
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
                arguments(12, DeviceType.SMART_CURTAIN),
                arguments(0, DeviceType.INVALID_DEVICE)
        );
    }

    public Stream<Arguments> valueDeviceAndDeviceTypeProvider(RoomService roomService, CouplingService couplingService) {
        return Stream.of(
                arguments(new Light(roomService, couplingService), DeviceType.LIGHT),
                arguments(new DimmableLight(roomService, couplingService), DeviceType.DIMMABLE_LIGHT),
                arguments(new Switch(roomService, couplingService), DeviceType.SWITCH),
                arguments(new DimmableSwitch(roomService, couplingService), DeviceType.DIMMABLE_SWITCH),
                arguments(new StatelessDimmableSwitch(roomService, couplingService), DeviceType.STATELESS_DIMMABLE_SWITCH),
                arguments(new SmartPlug(roomService, couplingService), DeviceType.SMART_PLUG),
                arguments(new HumiditySensor(roomService, couplingService), DeviceType.HUMIDITY_SENSOR),
                arguments(new LightSensor(roomService, couplingService), DeviceType.LIGHT_SENSOR),
                arguments(new TempSensor(roomService, couplingService), DeviceType.TEMP_SENSOR),
                arguments(new MotionSensor(roomService, couplingService), DeviceType.MOTION_SENSOR),
                arguments(new SmartCurtain(roomService, couplingService), DeviceType.SMART_CURTAIN)
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
    @Disabled(value = "fix static argument provider")
    void TestDeviceClassToDeviceType(Device device, DeviceType type) {
        assertEquals(DeviceType.deviceToDeviceType(device), type);
        assertEquals(DeviceType.deviceClassToDeviceType(User.class), DeviceType.INVALID_DEVICE);
    }

    @ParameterizedTest
    @MethodSource("valueDeviceAndDeviceTypeProvider")
    @DisplayName("Create a Device based on given DeviceType")
    @Disabled(value = "fix static argument provider")
    void TestMakeDevice(Device device, DeviceType type) {
        assertEquals(DeviceType.makeDevice(type, roomService, couplingService).getClass(), device.getClass());
        assertNull(DeviceType.makeDevice(DeviceType.INVALID_DEVICE, roomService, couplingService));
    }
}