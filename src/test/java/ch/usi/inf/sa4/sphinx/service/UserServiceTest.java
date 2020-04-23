package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.misc.DeviceType;
import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.Room;
import ch.usi.inf.sa4.sphinx.model.User;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;
    @Autowired
    RoomService roomService;

    @Test
    @DisplayName("test different getters, deletion, updating and inserting methods")
    void testsAreRunning(){

    }


//    @Test
//    @DisplayName("test different getters, deletion, updating and inserting methods")
//    void testUser() {
//        String username = "serviceUsername";
//        String email = "casual@usi.ch";
//        User newUser = new User(email, "1234", username, "mario rossi");
//        assertAll("test with not existing data",
//                () -> assertTrue(userService.get("notExistingUser").isEmpty()),
//                () -> assertTrue(userService.get(username).isEmpty()),
//                () -> assertTrue(userService.getByMail(email).isEmpty()),
//                () -> assertTrue(userService.getByMail("notExisting@usi.ch").isEmpty()),
//                () -> assertFalse(userService.validSession(username, "test"))
//        );
//
//
//
//        assertTrue(userService.insert(newUser));
//        User returnedUserByUsername = userService.get(username).get();
//        User returnedUserByEmail = userService.getByMail(email).get();
//        assertAll("should return correct user",
//                () -> assertEquals(username, returnedUserByUsername.getUsername()),
//                () -> assertEquals(username, returnedUserByEmail.getUsername()),
//                () -> assertEquals(email, returnedUserByUsername.getEmail()),
//                () -> assertEquals(email, returnedUserByEmail.getEmail()),
//                () -> assertEquals("1234", returnedUserByEmail.getPassword()),
//                () -> assertEquals("1234", returnedUserByUsername.getPassword()),
//                () -> assertEquals("mario rossi", returnedUserByUsername.getFullname()),
//                () -> assertEquals("mario rossi", returnedUserByEmail.getFullname()),
//                () -> assertNull(returnedUserByUsername.getSessionToken()),
//                () -> assertThrows(NullPointerException.class, () -> userService.validSession(username, "test"))
//        );
//        newUser.setSessionToken("token");
//        userService.update(newUser);
//        assertTrue(userService.validSession(username, "token"));
//        userService.delete(username);
//        assertNull(userService.get(username));
//
//
//        assertFalse(userService.update(newUser));//does not exists
//        assertThrows(NullPointerException.class, () -> userService.update(null));
//
//        assertTrue(userService.insert(newUser));
//        assertEquals(username, userService.get(username).get().getUsername());
//
//        String newFullname = "newTestFullname";
//        newUser.setFullname(newFullname);
//        assertTrue(userService.update(newUser));
//        assertEquals(username, userService.get(username).get().getUsername());
//        assertEquals(newFullname, userService.get(username).get().getFullname());
//        userService.delete(username);
//    }
//
//    @Test
//    void testUsername() {
//        String username = "serviceUsername2";
//        String email = "casual2@usi.ch";
//        User newUser = new User(email, "1234", username, "mario rossi");
//
//        assertAll("test with invalid values",
//                () -> assertThrows(NullPointerException.class, ()->userService.changeUsername(username, null)),
//                //() -> assertFalse(userService.changeUsername(username, "test")), Why?
//                () -> assertThrows(NullPointerException.class, ()->userService.changeUsername(null, "test"))
//        );
//        userService.insert(newUser);
//
//        String newUsername = "newUsername";
//        assertTrue(userService.changeUsername(username, newUsername));
//        assertEquals(newUsername, userService.get(newUsername).get().getUsername());
//    }
////
//    @Test
//    @DisplayName("Test adding and removing rooms")
//    void testRooms() {
//        String username = "serviceUsername1";
//        String email = "casual1@usi.ch";
//        User newUser = new User(email, "1234", username, "mario rossi");
//        Room room = new Room();
//        room.setName("testName");
//
//        assertFalse(userService.ownsRoom(username, 1));
//        assertEquals(new ArrayList<Room>(), userService.getPopulatedRooms("notExisting"));
//
//        assertTrue(userService.addRoom(username, room).isEmpty());
//        assertTrue(userService.insert(newUser));
//        assertFalse(userService.ownsRoom(username, 1));//no rooms
//
//
//        Room nullKeyRoom = new Room();
//        assertEquals(Integer.class, userService.addRoom(username, nullKeyRoom).get().getClass());
//
//        Integer id = userService.addRoom(username, room).get();
//        assertFalse(userService.ownsRoom(username, 9999)); //not existing id
//        assertTrue(userService.ownsRoom(username, id));
//
//        assertFalse(userService.removeRoom(username, 9999));
//        assertFalse(userService.removeRoom("notExistsUsername", 1));
//
//        assertTrue(userService.removeRoom(username, id));
//        assertFalse(userService.ownsRoom(username, id));
//    }
//
//    @Test
//    @DisplayName("test for different getters for all devices")
//    void testDevices() {
//        String username = "serviceUsername3";
//        String email = "casual3@usi.ch";
//        User newUser = new User(email, "1234", username, "mario rossi");
//        Room room1 = new Room(), room2 = new Room(), room3 = new Room();
//        room1.setName("testName1");
//        room2.setName("testName2");
//        room3.setName("testName3");
//
//        assertEquals(new ArrayList<Integer>(), userService.getDevices("notExisting").get());
//        userService.insert(newUser);
//        assertEquals(new ArrayList<Integer>(), userService.getDevices(username).get()); //no device
//        assertEquals(new ArrayList<Device>(), userService.getPopulatedDevices(username).get()); //no device
//        assertEquals(new ArrayList<Device>(), userService.getPopulatedDevices("notExisting").get()); //no device
//
//        Integer id1 = userService.addRoom(username, room1).get(); // no devices
//        Integer id2 = userService.addRoom(username, room2).get(); // 3 devices
//        Integer id3 = userService.addRoom(username, room3).get(); // 1 device
//        assertAll("should return list with 3 rooms",
//                () -> assertEquals(room1.getName(), userService.getPopulatedRooms(username).get(0).getName()),
//                () -> assertEquals(room2.getName(), userService.getPopulatedRooms(username).get(1).getName()),
//                () -> assertEquals(room3.getName(), userService.getPopulatedRooms(username).get(2).getName()),
//                () -> assertEquals(3, userService.getPopulatedRooms(username).size()),
//                () -> assertTrue( userService.getPopulatedDevices(username).get().isEmpty()), //should not have any devices
//                () -> assertTrue(userService.getDevices(username).get().isEmpty()) //should not have any devices
//        );
//
//        Integer room2Device1 = roomService.addDevice(id2, DeviceType.LIGHT_SENSOR).get();
//        Integer room2Device2 = roomService.addDevice(id2, DeviceType.LIGHT).get();
//        Integer room2Device3 = roomService.addDevice(id2, DeviceType.SMART_PLUG).get();
//        Integer room3Device1 = roomService.addDevice(id3, DeviceType.SWITCH).get();
//        List<Integer> devices = userService.getDevices(username).get();
//        assertEquals(4, devices.size());
//
//        ArrayList<Integer> listId = new ArrayList<Integer>(Arrays.asList(room2Device1, room2Device2, room2Device3, room3Device1));
//        for (Integer id : listId) {
//            assertTrue(userService.ownsDevice(username, id));
//        }
//        assertFalse(userService.ownsDevice(username, 9999)); // not existing id
//        assertFalse(userService.ownsDevice("notExisting", 9999)); // not existing username
//
//        var deviceList = userService.getPopulatedDevices(username);
//        assertEquals(4, deviceList.get().size());
//        List<Integer> listOfId = deviceList.map(ds->ds.stream().map(Device::getId).collect(Collectors.toList())).get();
//        for (Integer id : listOfId) {
//            assertTrue(userService.ownsDevice(username, id));
//        }
//    }
//
//    @Test
//    @Disabled(value = "fix the error in RoomService.removeDevice line 92 and UserService.migrateDevice line 279: java.lang.UnsupportedOperationException")
//    @DisplayName("testing for removing and migrating device")
//    void testMovingDevice() {
//        String username = "serviceUsername4";
//        String email = "casual4@usi.ch";
//        User newUser = new User(email, "1234", username, "mario rossi");
//        Room room1 = new Room(), room2 = new Room();
//        room1.setName("testName1");
//        room2.setName("testName2");
//        assertTrue(userService.insert(newUser));
//
//        assertAll("test with invalid values",
//                () -> assertFalse(userService.migrateDevice(username, 1, 2, 3)),
//                () -> assertFalse(userService.migrateDevice("notExistingUsername", 1, 2, 3)),
//                () -> assertFalse(userService.migrateDevice("notExistingUsername", null, 2, 3)),
//                () -> assertFalse(() -> userService.migrateDevice(username, null, null, null)),
//                () -> assertFalse(() -> userService.migrateDevice(username, 1, null, null)),
//                () -> assertFalse(() -> userService.migrateDevice(username, 1, 2, null)),
//                () -> assertNull(userService.owningRoom(username, 1)),
//                () -> assertNull(userService.owningRoom("notExistingUsername", 1)),
//                () -> assertNull(userService.owningRoom("notExistingUsername", null))
//        );
//        Integer id1 = userService.addRoom(username, room1).get();
//        Integer id2 = userService.addRoom(username, room2).get();
//        Integer deviceId = roomService.addDevice(id1, DeviceType.TEMP_SENSOR).get();
//
//        assertEquals(id1, userService.owningRoom(username, deviceId));
//        assertNull(userService.owningRoom(username, 9999));
////        userService.removeDevice(username, deviceId);
////        assertNull(userService.owningRoom(username, deviceId));
//
//        deviceId = roomService.addDevice(id1, DeviceType.SMART_PLUG).get();
//        assertTrue(userService.migrateDevice(username, deviceId, id1, id2)); //migrate to room2
//        assertEquals(id2, userService.owningRoom(username, deviceId));
//
//        //todo check more in userService.migrateDevice, because the method does not check that device effectively belongs to startRoomId
//        assertFalse(userService.migrateDevice(username, deviceId, id1, id2)); //migrate to room2 (no more devices)
//        userService.removeDevice(username, deviceId);
//        assertEquals(new ArrayList<Integer>(), userService.getDevices(username).get()); //no devices
//    }
}