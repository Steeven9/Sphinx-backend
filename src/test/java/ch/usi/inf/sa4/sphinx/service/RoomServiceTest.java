package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.demo.DummyDataAdder;
import ch.usi.inf.sa4.sphinx.misc.DeviceType;
import ch.usi.inf.sa4.sphinx.model.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RoomServiceTest {

    @Autowired
    RoomService roomService;
    @Autowired
    UserService userService;
    @Autowired
    RoomStorage roomStorage;
    @Autowired
    DummyDataAdder dummyDataAdder;

    private static final String username = "testUser";
    User user;


    @BeforeAll
    void beforeAll(){
        userService.delete(username);
        userService.delete("User2");
    }

    @BeforeEach
    void setUp() {
        User newUser = new User("test@mail.com", "1234", username, "mario rossi");
        userService.insert(newUser);
        user = userService.get(username).get();
    }

    @AfterEach
    void clean() {
        userService.delete(username);
        userService.delete("User2");
    }

    @Test
    @DisplayName("Tests both get and update methods")
    void testGetAndUpdate() {
        assertTrue(roomService.get(9999).isEmpty());
        Room newRoom = new Room();
        newRoom.setName("testName");
        Integer roomId = userService.addRoom(username, newRoom).get();
        Room storageRoom = roomService.get(roomId).get();


        assertNotSame(storageRoom, newRoom); //does not point to the same object
        assertEquals("testName", storageRoom.getName());
//
        String name = "secondTestName";//test update method
        storageRoom.setName(name);
//
        assertTrue(roomService.update(storageRoom));
        storageRoom = roomService.get(storageRoom.getId()).get();
        assertEquals(name, storageRoom.getName());

        assertFalse(roomService.update(new Room()));
        assertThrows(NullPointerException.class, () -> roomService.update(null));
    }

    //    @Disabled(value ="the test should not rely on the order of the ids assigned")
    @Test
    void testGetPopulatedDevices() {
        assertTrue(roomService.getPopulatedDevices(9999).isEmpty());//not existing id
        Integer id = userService.addRoom(username, new Room()).get();
        roomService.addDevice(id, DeviceType.TEMP_SENSOR);
        roomService.addDevice(id, DeviceType.SWITCH);
        List<Device> result = roomService.getPopulatedDevices(id).get();
        assertAll(
                () -> assertEquals(2, result.size()),
                () -> assertEquals(DeviceType.deviceClassToDeviceType(TempSensor.class), DeviceType.deviceToDeviceType(result.get(0))),
                () -> assertEquals(DeviceType.deviceClassToDeviceType(Switch.class), DeviceType.deviceToDeviceType(result.get(1)))
        );
    }

    @Test
    void testAddDevice() {
        assertAll("test for invalid values",
                () -> assertThrows(NullPointerException.class, () -> roomService.addDevice(null, DeviceType.DIMMABLE_LIGHT)),
                () -> assertThrows(NullPointerException.class, () -> roomService.addDevice(null, null)),
                () -> assertThrows(NullPointerException.class, () -> roomService.addDevice(2, null)),
                () -> assertTrue(roomService.addDevice(1, DeviceType.INVALID_DEVICE).isEmpty()),
                () -> assertTrue(roomService.addDevice(3333, DeviceType.DIMMABLE_LIGHT).isEmpty())
        );

        Integer id = userService.addRoom(username, new Room()).get();
        assertEquals(0, roomService.getPopulatedDevices(id).get().size());
        roomService.addDevice(id, DeviceType.MOTION_SENSOR);
        assertAll("should add a new device",
                () -> assertEquals(1, roomService.getPopulatedDevices(id).get().size()),
                () -> assertEquals(DeviceType.MOTION_SENSOR, DeviceType.deviceToDeviceType(roomService.getPopulatedDevices(id).get().get(0)))
        );
    }
//
    @Test
    void testRemoveDevice() {
        assertAll("test for invalid values",
                () -> assertThrows(NullPointerException.class, () ->roomService.removeDevice(null, null)),
                () -> assertThrows(NullPointerException.class, () ->roomService.removeDevice(10, null)),
                () -> assertThrows(NullPointerException.class, () ->roomService.removeDevice(null, 1000)),
                () -> assertFalse(roomService.removeDevice(10, 999999)),
                () -> assertFalse(roomService.removeDevice(1, 999999))
        );
//
        //removing a device

        Room newRoom = new Room();
        userService.addRoom(user.getUsername(), newRoom );
        Integer roomId = userService.addRoom(user.getUsername(), newRoom ).get();
        Integer devId = roomService.addDevice(roomId, DeviceType.MOTION_SENSOR).get();
        assertTrue(roomService.removeDevice(roomId, devId));   //java.lang.UnsupportedOperationException for deviceId
    }
}