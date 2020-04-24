package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.Demo.DummyDataAdder;
import ch.usi.inf.sa4.sphinx.misc.DeviceType;
import ch.usi.inf.sa4.sphinx.service.RoomService;
import ch.usi.inf.sa4.sphinx.service.UserService;
import ch.usi.inf.sa4.sphinx.view.*;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class SerialiserTest {

    private final String username = "randomUsername";
    @Autowired
    Serialiser serialiser;
    @Autowired
    UserService userService;
    @Autowired
    DummyDataAdder dummyDataAdder;

    private Device device;
    private User user;
    private Room room;
    @Autowired
    RoomService roomService;

    @BeforeAll
     void createDeviceAndUser() {
  //      dummyDataAdder.user1();
//        user =userService.get("user1").get();
        dummyDataAdder.user1();
        user = userService.get("user1").get();
//        room = user.getRooms().get(0);
        //Hibernate.initialize(user.getRooms());
        //var rooms = user.getRooms();
        //boolean ooo =  Hibernate.isInitialized(rooms);
//        room = rooms.get(0);
       // var devices = userService.getPopulatedDevices("user");
       // device = devices.get().get(0);
    }

    @BeforeEach
    public void readRoomAndDevice(){
        room = user.getRooms().get(0);
        device = room.getDevices().get(0);
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


    //Device cant exist without a Room
//    /**
//     * First test <code>serialiser.serialiseDevice(Device, User)</code>, where device does not
//     * belong to any User. Then test the case when the device belongs to a given User.
//     */
//    @Test
//    void testSerialiseDeviceAssociatedToRoom() {
//        SerialisableDevice serialisableDevice = serialiser.serialiseDevice(device, user);
//        assertNotNull(serialisableDevice);
//        assertEquals(SerialisableDevice.class, serialisableDevice.getClass());
//        assertAll("this device does not belong to any room",
//                () -> assertNull(serialisableDevice.roomId),
//                () -> assertNull(serialisableDevice.roomName),
//                () -> assertNull(serialisableDevice.userName));
//        userService.insert(user);
//        userService.addRoom(username, room);
//        roomService.addDevice(2, DeviceType.deviceToDeviceType(device));
//        room.addDevice(device);
//        SerialisableDevice serialisableDeviceWithRoom = serialiser.serialiseDevice(device, user);
//        assertAll("this device does not belong to any room",
//                () -> assertEquals(serialisableDeviceWithRoom.roomId, room.getId()),
//                () -> assertEquals(serialisableDeviceWithRoom.roomName, room.getName()),
//                () -> assertEquals(serialisableDeviceWithRoom.userName, username));
//    }
}
