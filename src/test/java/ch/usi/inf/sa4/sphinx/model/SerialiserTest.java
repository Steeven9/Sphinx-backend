package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.Demo.DummyDataAdder;
import ch.usi.inf.sa4.sphinx.misc.DeviceType;
import ch.usi.inf.sa4.sphinx.service.CouplingService;
import ch.usi.inf.sa4.sphinx.service.RoomService;
import ch.usi.inf.sa4.sphinx.service.UserService;
import ch.usi.inf.sa4.sphinx.view.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SerialiserTest {

    private final String username = "randomUsername";
    @Autowired
    Serialiser serialiser;
    @Autowired
    UserService userService;
    @Autowired
    DummyDataAdder dummyDataAdder;

    Device device;
    User user;
    Room room;
    @Autowired
    RoomService roomService;
    @Autowired
    CouplingService couplingService;

    @BeforeEach
    void createDeviceAndUser() {
        device = new Light(roomService, couplingService);
        this.device.setId(1);
        room = new Room();
        this.room.setId(2);
        user = new User("randomEmail", "randomPassword", username, "randomFullname");
        userService.insert(user);
        userService.addRoom(username, this.room);

    }

    @Test
    @DisplayName("Transform this device in a serialiseDevice")
    void isSerializedDevice() {
        SerialisableDevice serialisableDevice = serialiser.serialiseDevice(device);
        assertNotNull(serialisableDevice);
        assertEquals(SerialisableDevice.class, serialisableDevice.getClass());
    }

    @Test
    @DisplayName("Transform this room in a serialiseRoom")
    void isSerialisableRoom() {
        SerialisableRoom serialisableRoom = serialiser.serialiseRoom(room);
        assertNotNull(serialisableRoom);
        assertEquals(SerialisableRoom.class, serialisableRoom.getClass());
    }

    @Test
    @DisplayName("Transform this user in a serialiseUser")
    void isSerialisableUser() {
        SerialisableUser serialisableUser = serialiser.serialiseUser(user);
        assertNotNull(serialisableUser);
        assertEquals(SerialisableUser.class, serialisableUser.getClass());
    }

    /**
     * First test <code>serialiser.serialiseDevice(Device, User)</code>, where device does not
     * belong to any User. Then test the case when the device belongs to a given User.
     */
    @Test
    void testSerialiseDeviceAssociatedToRoom() {
        SerialisableDevice serialisableDevice = serialiser.serialiseDevice(device, user);
        assertNotNull(serialisableDevice);
        assertEquals(SerialisableDevice.class, serialisableDevice.getClass());
        assertAll("this device does not belong to any room",
                () -> assertNull(serialisableDevice.roomId),
                () -> assertNull(serialisableDevice.roomName),
                () -> assertNull(serialisableDevice.userName));
        userService.insert(user);
        userService.addRoom(username, room);
        roomService.addDevice(2, DeviceType.deviceToDeviceType(device));
        room.addDevice(device.getId());
        SerialisableDevice serialisableDeviceWithRoom = serialiser.serialiseDevice(device, user);
        assertAll("this device does not belong to any room",
                () -> assertEquals(serialisableDeviceWithRoom.roomId, room.getId()),
                () -> assertEquals(serialisableDeviceWithRoom.roomName, room.getName()),
                () -> assertEquals(serialisableDeviceWithRoom.userName, username));
    }
}
