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
    void testUser() {
        String username = "serviceUsername";
        String email = "casual@usi.ch";
        User newUser = new User(email, "1234", username, "mario rossi");
        assertAll("test with not existing data",
                () -> assertNull(userService.get("notExistingUser")),
                () -> assertNull(userService.get(username)),
                () -> assertNull(userService.getByMail(email)),
                () -> assertNull(userService.getByMail("notExisting@usi.ch")),
                () -> assertFalse(userService.validSession(username, "test"))
        );

        assertTrue(userService.insert(newUser));
        User returnedUserByUsername = userService.get(username);
        User returnedUserByEmail = userService.getByMail(email);
        assertAll("should return correct user",
                () -> assertEquals(username, returnedUserByUsername.getUsername()),
                () -> assertEquals(username, returnedUserByEmail.getUsername()),
                () -> assertEquals(email, returnedUserByUsername.getEmail()),
                () -> assertEquals(email, returnedUserByEmail.getEmail()),
                () -> assertEquals("1234", returnedUserByEmail.getPassword()),
                () -> assertEquals("1234", returnedUserByUsername.getPassword()),
                () -> assertEquals("mario rossi", returnedUserByUsername.getFullname()),
                () -> assertEquals("mario rossi", returnedUserByEmail.getFullname()),
                () -> assertNull(returnedUserByUsername.getSessionToken()),
                () -> assertThrows(NullPointerException.class, () -> userService.validSession(username, "test"))
        );
        newUser.setSessionToken("token");
        userService.update(newUser);
        assertTrue(userService.validSession(username, "token"));
        userService.delete(username);
        assertNull(userService.get(username));


        assertFalse(userService.update(newUser));//does not exists
        assertThrows(NullPointerException.class, () -> userService.update(null));

        assertTrue(userService.insert(newUser));
        assertEquals(username, userService.get(username).getUsername());

        String newFullname = "newTestFullname";
        newUser.setFullname(newFullname);
        assertTrue(userService.update(newUser));
        assertEquals(username, userService.get(username).getUsername());
        assertEquals(newFullname, userService.get(username).getFullname());
        userService.delete(username);
    }

    @Test
    @Disabled(value = "fix changeUsername: change effectively the username")
    void testUsername() {
        String username = "serviceUsername2";
        String email = "casual2@usi.ch";
        User newUser = new User(email, "1234", username, "mario rossi");

        assertAll("test with invalid values",
                () -> assertFalse(userService.changeUsername(username, null)),
                () -> assertFalse(userService.changeUsername(username, "test")),
                () -> assertFalse(userService.changeUsername(null, "test"))
        );
        userService.insert(newUser);

        String newUsername = "newUsername";
        assertTrue(userService.changeUsername(username, newUsername));
        assertEquals(newUsername, userService.get(newUsername).getUsername());
    }

    @Test
    @DisplayName("Test adding and removing rooms")
    void testRooms() {
        String username = "serviceUsername1";
        String email = "casual1@usi.ch";
        User newUser = new User(email, "1234", username, "mario rossi");
        Room room = new Room();
        room.setName("testName");

        assertFalse(userService.ownsRoom(username, 1));
        assertEquals(new ArrayList<Integer>(), userService.getPopulatedRooms("notExisting"));

        assertNull(userService.addRoom(username, room));
        assertTrue(userService.insert(newUser));
        assertFalse(userService.ownsRoom(username, 1));//no rooms

        Room lockedKeyRoom = new Room();
        lockedKeyRoom.lockKey();
        assertNull(userService.addRoom(username, lockedKeyRoom));

        Room nullKeyRoom = new Room();
        nullKeyRoom.setKey(null);
        assertEquals(Integer.class, userService.addRoom(username, nullKeyRoom).getClass());

        Integer id = userService.addRoom(username, room);
        assertFalse(userService.ownsRoom(username, 9999)); //not existing id
        assertTrue(userService.ownsRoom(username, id));

        assertFalse(userService.removeRoom(username, 9999));
        assertFalse(userService.removeRoom("notExistsUsername", 1));

        assertTrue(userService.removeRoom(username, id));
        assertFalse(userService.ownsRoom(username, id));
    }

    @Test
    @DisplayName("test for different getters for all devices")
    void testDevices() {
        String username = "serviceUsername3";
        String email = "casual3@usi.ch";
        User newUser = new User(email, "1234", username, "mario rossi");
        Room room1 = new Room(), room2 = new Room(), room3 = new Room();
        room1.setName("testName1");
        room2.setName("testName2");
        room3.setName("testName3");

        assertEquals(new ArrayList<Integer>(), userService.getDevices("notExisting"));
        userService.insert(newUser);
        assertEquals(new ArrayList<Integer>(), userService.getDevices(username)); //no device
        assertEquals(new ArrayList<Device>(), userService.getPopulatedDevices(username)); //no device
        assertEquals(new ArrayList<Device>(), userService.getPopulatedDevices("notExisting")); //no device

        Integer id1 = userService.addRoom(username, room1); // no devices
        Integer id2 = userService.addRoom(username, room2); // 3 devices
        Integer id3 = userService.addRoom(username, room3); // 1 device
        assertAll("should return list with 3 rooms",
                () -> assertEquals(room1.getName(), userService.getPopulatedRooms(username).get(0).getName()),
                () -> assertEquals(room2.getName(), userService.getPopulatedRooms(username).get(1).getName()),
                () -> assertEquals(room3.getName(), userService.getPopulatedRooms(username).get(2).getName()),
                () -> assertEquals(3, userService.getPopulatedRooms(username).size()),
                () -> assertEquals(0, userService.getPopulatedDevices(username).size()), //should not have any devices
                () -> assertEquals(0, userService.getDevices(username).size()) //should not have any devices
        );

        Integer room2Device1 = roomService.addDevice(id2, DeviceType.LIGHT_SENSOR);
        Integer room2Device2 = roomService.addDevice(id2, DeviceType.LIGHT);
        Integer room2Device3 = roomService.addDevice(id2, DeviceType.SMART_PLUG);
        Integer room3Device1 = roomService.addDevice(id3, DeviceType.SWITCH);
        List<Integer> devices = userService.getDevices(username);
        assertEquals(4, devices.size());

        ArrayList<Integer> listId = new ArrayList<Integer>(Arrays.asList(room2Device1, room2Device2, room2Device3, room3Device1));
        for (Integer id : listId) {
            assertTrue(userService.ownsDevice(username, id));
        }
        assertFalse(userService.ownsDevice(username, 9999)); // not existing id
        assertFalse(userService.ownsDevice("notExisting", 9999)); // not existing username

        var deviceList = userService.getPopulatedDevices(username);
        assertEquals(4, deviceList.size());
        List<Integer> listOfId = deviceList.stream().map(Device::getId).collect(Collectors.toList());
        for (Integer id : listOfId) {
            assertTrue(userService.ownsDevice(username, id));
        }
    }

    @Test
    @Disabled(value = "fix the error in RoomService.removeDevice line 92 and UserService.migrateDevice line 279: java.lang.UnsupportedOperationException")
    @DisplayName("testing for removing and migrating device")
    void testMovingDevice() {
        String username = "serviceUsername4";
        String email = "casual4@usi.ch";
        User newUser = new User(email, "1234", username, "mario rossi");
        Room room1 = new Room(), room2 = new Room();
        room1.setName("testName1");
        room2.setName("testName2");
        assertTrue(userService.insert(newUser));

        assertAll("test with invalid values",
                () -> assertFalse(userService.migrateDevice(username, 1, 2, 3)),
                () -> assertFalse(userService.migrateDevice("notExistingUsername", 1, 2, 3)),
                () -> assertFalse(userService.migrateDevice("notExistingUsername", null, 2, 3)),
                () -> assertFalse(() -> userService.migrateDevice(username, null, null, null)),
                () -> assertFalse(() -> userService.migrateDevice(username, 1, null, null)),
                () -> assertFalse(() -> userService.migrateDevice(username, 1, 2, null)),
                () -> assertNull(userService.owningRoom(username, 1)),
                () -> assertNull(userService.owningRoom("notExistingUsername", 1)),
                () -> assertNull(userService.owningRoom("notExistingUsername", null))
        );
        Integer id1 = userService.addRoom(username, room1);
        Integer id2 = userService.addRoom(username, room2);
        Integer deviceId = roomService.addDevice(id1, DeviceType.TEMP_SENSOR);

        assertEquals(id1, userService.owningRoom(username, deviceId));
        assertNull(userService.owningRoom(username, 9999));
//        userService.removeDevice(username, deviceId);
//        assertNull(userService.owningRoom(username, deviceId));

        deviceId = roomService.addDevice(id1, DeviceType.SMART_PLUG);
        assertTrue(userService.migrateDevice(username, deviceId, id1, id2)); //migrate to room2
        assertEquals(id2, userService.owningRoom(username, deviceId));

        //todo check more in userService.migrateDevice, because the method does not check that device effectively belongs to startRoomId
        assertFalse(userService.migrateDevice(username, deviceId, id1, id2)); //migrate to room2 (no more devices)
        userService.removeDevice(username, deviceId);
        assertEquals(new ArrayList<Integer>(), userService.getDevices(username)); //no devices
    }
}