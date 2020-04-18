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
class RoomServiceTest {

    @Autowired
    RoomService roomService;
    @Autowired
    VolatileRoomStorage roomStorage;
    @Autowired
    DummyDataAdder dummyDataAdder;


    @Test
    @DisplayName("Tests both get and update methods")
    void testGetAndUpdate() {
        assertNull(roomService.get(9999));

        Room room = new Room();
        room.setName("testName");
        Integer id = roomStorage.insert(room);
        Room returnedRoom = roomService.get(id);
        assertNotEquals(room, returnedRoom); //does not point to the same object
        assertEquals("testName", returnedRoom.getName());

        String name = "secondTestName";//test update method
        room.setName(name);

        assertTrue(roomService.update(room));
        returnedRoom = roomService.get(id);
        assertEquals(name, returnedRoom.getName());

        assertFalse(roomService.update(new Room()));
    }

    @Test
    void testGetPopulatedDevices() {
        assertEquals(new ArrayList<>(), roomService.getPopulatedDevices(9999));//not existing id
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
    void testAddDevice() {
        assertAll("test for invalid values",
                () -> assertNull(roomService.addDevice(null, DeviceType.DIMMABLE_LIGHT)),
                () -> assertThrows(NullPointerException.class, () -> roomService.addDevice(null, null)),
                () -> assertThrows(NullPointerException.class, () -> roomService.addDevice(2, null)),
                () -> assertNull(roomService.addDevice(1, DeviceType.INVALID_DEVICE)),
                () -> assertNull(roomService.addDevice(333, DeviceType.DIMMABLE_LIGHT))
        );
        dummyDataAdder.user2();
        assertEquals(1, roomService.getPopulatedDevices(5).size());
        roomService.addDevice(5, DeviceType.MOTION_SENSOR);
        assertAll("should add a new device",
                () -> assertEquals(2, roomService.getPopulatedDevices(5).size()),
                () -> assertEquals(DeviceType.MOTION_SENSOR, DeviceType.deviceToDeviceType(roomService.getPopulatedDevices(5).get(1)))
        );
    }

    @Test
    @Disabled(value = "fix the error in RoomService.removeDevice line 92. java.lang.UnsupportedOperationException")
    void testRemoveDevice() {
        assertAll("test for invalid values",
                () -> assertFalse(roomService.removeDevice(null, null)),
                () -> assertFalse(roomService.removeDevice(10, null)),
                () -> assertFalse(roomService.removeDevice(null, 1000)),
                () -> assertFalse(roomService.removeDevice(10, 1000)),
                () -> assertFalse(roomService.removeDevice(1, 1000)), //java.lang.UnsupportedOperationException for deviceId
                () -> assertFalse(roomService.removeDevice(10, 5))
        );

        //removing a device
        Integer key = roomStorage.insert(new Room());
        Integer devId = roomService.addDevice(key, DeviceType.MOTION_SENSOR);
        assertTrue(roomService.removeDevice(key, devId));   //java.lang.UnsupportedOperationException for deviceId
    }
}