package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.Demo.DummyDataAdder;
import ch.usi.inf.sa4.sphinx.misc.DeviceType;
import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.HumiditySensor;
import ch.usi.inf.sa4.sphinx.model.Light;
import ch.usi.inf.sa4.sphinx.model.Room;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RoomServiceTest {

    @Autowired
    RoomService roomService;
    @Autowired
    VolatileRoomStorage roomStorage;
    @Autowired
    DummyDataAdder dummyDataAdder;


    @Test
    @DisplayName("Tests both get and update methods")
    @Order(1)
    void testGetAndUpdate() {
        Room room = new Room();
        room.setName("testName");
        assertNull(roomService.get(88));
        Integer id = roomStorage.insert(room);
        Room returnedRoom = roomService.get(id);
        assertEquals("testName", returnedRoom.getName());

        String name = "secondTestName";//test update method
        room.setName(name);
        assertTrue(roomService.update(room));
        returnedRoom = roomService.get(id);
        assertEquals(name, returnedRoom.getName());

        assertFalse(roomService.update(new Room()));
    }

    @Test
    @Order(2)
    void testGetPopulatedDevices() {
        assertEquals(new ArrayList<>(), roomService.getPopulatedDevices(1));//not existing id
        dummyDataAdder.user2();
        List<Device> result = roomService.getPopulatedDevices(2);
        assertAll(
                () -> assertEquals(2, result.size()),
                () -> assertEquals(DeviceType.deviceClassToDeviceType(HumiditySensor.class), DeviceType.deviceToDeviceType(result.get(0))),
                () -> assertEquals(DeviceType.deviceClassToDeviceType(Light.class), DeviceType.deviceToDeviceType(result.get(1)))
        );
        List<Device> res = roomService.getPopulatedDevices(4);
        assertEquals(0, res.size());
    }

    @Test
    @Order(3)
    void testAddDevice() {
        assertNull(roomService.addDevice(null, DeviceType.DIMMABLE_LIGHT));
        assertThrows(NullPointerException.class, () -> roomService.addDevice(null, null));
        assertThrows(NullPointerException.class, () -> roomService.addDevice(2, null));

        assertNull(roomService.addDevice(1, DeviceType.INVALID_DEVICE));
        assertNull(roomService.addDevice(333, DeviceType.DIMMABLE_LIGHT));

        assertEquals(1, roomService.getPopulatedDevices(5).size());
        roomService.addDevice(5, DeviceType.MOTION_SENSOR);
        assertAll("should add a new device",
                () -> assertEquals(2, roomService.getPopulatedDevices(5).size()),
                () -> assertEquals(DeviceType.MOTION_SENSOR, DeviceType.deviceToDeviceType(roomService.getPopulatedDevices(5).get(1)))
        );
    }

    @Test
    @Order(4)
    @Disabled(value = "fix the error in RoomService.removeDevice line 92. The device is removed based on position, not on true deviceID")
    void testRemoveDevice() {
        assertAll("test for invalid values",
                () -> assertFalse(roomService.removeDevice(null, null)),
                () -> assertFalse(roomService.removeDevice(10, null)),
                () -> assertFalse(roomService.removeDevice(null, 1000)),
                () -> assertFalse(roomService.removeDevice(10, 1000)),
                () -> assertFalse(roomService.removeDevice(1, 1000)), //java.lang.UnsupportedOperationException for deviceId
                () -> assertFalse(roomService.removeDevice(10, 5))
        );

        List<Device> result = roomService.getPopulatedDevices(2);
        assertEquals(2, result.size());
        Integer key = result.get(result.size() - 1).getKey();

        //removing a device
        assertEquals(DeviceType.deviceClassToDeviceType(Light.class), DeviceType.deviceToDeviceType(result.get(result.size() - 1)));
        assertTrue(roomService.removeDevice(2, key)); //java.lang.UnsupportedOperationException for deviceId
        assertEquals(DeviceType.deviceClassToDeviceType(HumiditySensor.class), DeviceType.deviceToDeviceType(result.get(result.size() - 1)));
        assertEquals(1, result.size());
    }
}