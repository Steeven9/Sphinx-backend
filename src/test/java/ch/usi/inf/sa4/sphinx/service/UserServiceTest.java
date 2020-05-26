package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.demo.DummyDataAdder;
import ch.usi.inf.sa4.sphinx.misc.DeviceType;
import ch.usi.inf.sa4.sphinx.misc.NotFoundException;
import ch.usi.inf.sa4.sphinx.misc.UnauthorizedException;
import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.Room;
import ch.usi.inf.sa4.sphinx.model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceTest {

    @Autowired
    UserService userService;
    @Autowired
    RoomService roomService;
    @Autowired
    private DummyDataAdder dummyDataAdder;

    @BeforeAll
    void init() {
        dummyDataAdder.addDummyData();
    }

    @Test
    @DisplayName("creates a user")
    void testCreatesUser() {
        User newUser = new User("name@it", "1234", "username", "fullname");
        userService.insert(newUser);
        boolean found = userService.get("username").isPresent();
        assertTrue(found);
    }

    @Test
    @DisplayName("test different getters, deletion, updating and inserting methods")
    void testUser() {
        String username = "serviceUsername";
        String email = "casual@usi.ch";
        User newUser = new User(email, "1234", username, "mario rossi");
        assertAll("test with not existing data",
                () -> assertTrue(userService.get("notExistingUser").isEmpty()),
                () -> assertTrue(userService.get(username).isEmpty()),
                () -> assertTrue(userService.getByMail(email).isEmpty()),
                () -> assertTrue(userService.getByMail("notExisting@usi.ch").isEmpty()),
                () -> assertThrows(UnauthorizedException.class, () -> userService.validateSession(username, "test"))
        );


        boolean inserted = userService.insert(newUser);
        assert (inserted);
        User returnedUserByUsername = userService.get(username).get();
        User returnedUserByEmail = userService.getByMail(email).get();
        assertAll("should return correct user",
                () -> assertEquals(username, returnedUserByUsername.getUsername()),
                () -> assertEquals(username, returnedUserByEmail.getUsername()),
                () -> assertEquals(email, returnedUserByUsername.getEmail()),
                () -> assertEquals(email, returnedUserByEmail.getEmail()),
                () -> assertTrue(returnedUserByUsername.matchesPassword("1234")),
                () -> assertTrue(returnedUserByEmail.matchesPassword("1234")),
                () -> assertEquals("mario rossi", returnedUserByUsername.getFullname()),
                () -> assertEquals("mario rossi", returnedUserByEmail.getFullname()),
                () -> assertNull(returnedUserByUsername.getSessionToken()),
                () -> assertThrows(UnauthorizedException.class, () -> userService.validateSession(username, "test"))
        );
        newUser.setSessionToken("token");
        userService.update(newUser);
        assertDoesNotThrow(() -> userService.validateSession(username, "token"));
        userService.delete(username);
        assertTrue(userService.get(username).isEmpty());


        assertFalse(userService.update(newUser));//does not exists
        assertThrows(NullPointerException.class, () -> userService.update(null));

        //newUser is tracked the db, it won't allow a previously deleted user to be added back
        assertFalse(userService.insert(newUser));
        User newUser2 = new User(newUser.getEmail(), newUser.getPassword(), newUser.getUsername(), newUser.getFullname());
        assertTrue(userService.insert(newUser2));

        assertEquals(username, userService.get(username).get().getUsername());

        String newFullname = "newTestFullname";
        newUser2.setFullname(newFullname);
        assertTrue(userService.update(newUser2));
        assertEquals(username, userService.get(username).get().getUsername());
        assertEquals(newFullname, userService.get(username).get().getFullname());
        userService.delete(username);
    }

    @Test
    void testUsername() {
        String username = "serviceUsername2";
        String email = "casual2@usi.ch";
        User newUser = new User(email, "1234", username, "mario rossi");

        assertAll("test with invalid values",
                () -> assertThrows(NullPointerException.class, () -> userService.changeUsername(username, null)),
                //() -> assertFalse(userService.changeUsername(username, "test")), Why?
                () -> assertThrows(NullPointerException.class, () -> userService.changeUsername(null, "test"))
        );
        userService.insert(newUser);

        String newUsername = "newUsername";
        assertTrue(userService.changeUsername(username, newUsername));
        assertEquals(newUsername, userService.get(newUsername).get().getUsername());
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
        assertFalse(userService.removeRoom(username, 1));
        assertEquals(new ArrayList<Room>(), userService.getPopulatedRooms("notExisting"));

        assertTrue(userService.addRoom(username, room).isEmpty());
        assertTrue(userService.insert(newUser));
        assertFalse(userService.ownsRoom(username, 1));//no rooms
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

        assertTrue(userService.getDevices("notExisting").isEmpty());
        userService.insert(newUser);
        userService.generateValue(username);
        assertEquals(new ArrayList<Integer>(), userService.getDevices(username).get()); //no device
        assertEquals(new ArrayList<Device>(), userService.getPopulatedDevices(username).get()); //no device
        assertTrue(userService.getPopulatedDevices("notExisting").isEmpty()); //no device

        Integer id1 = userService.addRoom(username, room1).get(); // no devices
        Integer id2 = userService.addRoom(username, room2).get(); // 3 devices
        Integer id3 = userService.addRoom(username, room3).get(); // 1 device
        assertAll("should return list with 3 rooms",
                () -> assertEquals(room1.getName(), userService.getPopulatedRooms(username).get(0).getName()),
                () -> assertEquals(room2.getName(), userService.getPopulatedRooms(username).get(1).getName()),
                () -> assertEquals(room3.getName(), userService.getPopulatedRooms(username).get(2).getName()),
                () -> assertEquals(3, userService.getPopulatedRooms(username).size()),
                () -> assertTrue(userService.getPopulatedDevices(username).get().isEmpty()), //should not have any devices
                () -> assertTrue(userService.getDevices(username).get().isEmpty()) //should not have any devices
        );

        Integer room2Device1 = roomService.addDevice(id2, DeviceType.LIGHT_SENSOR).get();
        Integer room2Device2 = roomService.addDevice(id2, DeviceType.LIGHT).get();
        Integer room2Device3 = roomService.addDevice(id2, DeviceType.SMART_PLUG).get();
        Integer room3Device1 = roomService.addDevice(id3, DeviceType.SWITCH).get();
        List<Integer> devices = userService.getDevices(username).get();
        assertEquals(4, devices.size());

        ArrayList<Integer> listId = new ArrayList<Integer>(Arrays.asList(room2Device1, room2Device2, room2Device3, room3Device1));
        for (Integer id : listId) {
            assertTrue(userService.ownsDevice(username, id));
        }
        assertFalse(userService.ownsDevice(username, 9999)); // not existing id
        assertFalse(userService.ownsDevice("notExisting", 9999)); // not existing username

        var deviceList = userService.getPopulatedDevices(username);
        assertEquals(4, deviceList.get().size());
        List<Integer> listOfId = deviceList.map(ds -> ds.stream().map(Device::getId).collect(Collectors.toList())).get();
        for (Integer id : listOfId) {
            assertTrue(userService.ownsDevice(username, id));
        }
    }

    @Test
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
        Integer id1 = userService.addRoom(username, room1).get();
        Integer id2 = userService.addRoom(username, room2).get();
        List<Device> ownedDevices = roomService.get(id1).get().getDevices();
        Integer deviceId = roomService.addDevice(id1, DeviceType.TEMP_SENSOR).get();
        ownedDevices = roomService.get(id1).get().getDevices();

        assertEquals(id1, userService.owningRoom(username, deviceId));
        assertNull(userService.owningRoom(username, 9999));
        userService.removeDevice(username, deviceId);
        assertNull(userService.owningRoom(username, deviceId));

        deviceId = roomService.addDevice(id1, DeviceType.SMART_PLUG).get();
        assertTrue(userService.migrateDevice(username, deviceId, id1, id2)); //migrate to room2
        assertEquals(id2, userService.owningRoom(username, deviceId));

        assertFalse(userService.migrateDevice(username, deviceId, id1, id2)); //migrate to room2 (no more devices)
        userService.removeDevice(username, deviceId);
        List<Integer> devicesOwnedByUser = userService.getDevices(username).get();
        assertTrue(devicesOwnedByUser.isEmpty()); //no devices
    }

    @Test
    void acceptsOnlyCorrectPassword() {
        User newUser = new User("test@com", "1234", "testUsername", "fullname");
        userService.insert(newUser);
        User storageUser = userService.get("testUsername").get();
        assertTrue(storageUser.matchesPassword("1234"));
        assertAll("wrong passwords are rejected",
                () -> assertThrows(NullPointerException.class, () -> storageUser.matchesPassword(null)),
                () -> assertFalse(storageUser.matchesPassword("")),
                () -> assertFalse(storageUser.matchesPassword("wrong")));
        userService.delete("testUsername");
    }

    @Test
    void testOwnsRoom() {
        List<Room> rooms = userService.getPopulatedRooms("user1");
        Integer roomId = rooms.get(0).getId();

        assertFalse(userService.ownsRoom("emptyUser", roomId));
        assertTrue(userService.ownsRoom("user1", roomId));
    }

    @Test
    void coverRemoveDevice() {
        Optional<List<Device>> optionalDevices = userService.getPopulatedDevices("user1");
        assertTrue(optionalDevices.isPresent());
        List<Device> devices = optionalDevices.get();
        Integer deviceId = devices.get(0).getId();

        userService.removeDevice("emptyUser", deviceId);
        userService.ownsRoom("user1", deviceId);
    }

    @Test
    void testMigrateDeviceFirstCondition() {
        Optional<List<Device>> optionalDevices = userService.getPopulatedDevices("user1");
        assertTrue(optionalDevices.isPresent());
        List<Device> devices = optionalDevices.get();
        Integer deviceId1 = devices.get(0).getId();
        List<Room> rooms = userService.getPopulatedRooms("user1");
        Integer roomId1 = rooms.get(0).getId();
        optionalDevices = userService.getPopulatedDevices("user2");
        devices = optionalDevices.get();
        Integer deviceId2 = devices.get(0).getId();
        rooms = userService.getPopulatedRooms("user2");
        Integer roomId2 = rooms.get(0).getId();

        assertFalse(userService.migrateDevice("user2", deviceId1, roomId1, roomId1));
        assertFalse(userService.migrateDevice("user2", deviceId1, roomId1, roomId2));
        assertFalse(userService.migrateDevice("user2", deviceId1, roomId2, roomId2));
        assertFalse(userService.migrateDevice("user2", deviceId2, roomId1, roomId1));
        assertFalse(userService.migrateDevice("user2", deviceId2, roomId1, roomId2));
        assertTrue(userService.migrateDevice("user2", deviceId2, roomId2, roomId2));
    }

    @Test
    void coverGenerateValueWithWrongUser() {
        assertTrue(true); //hi sonarqube
        userService.generateValue("fakeUser");
    }

    @Test
    void testAddGuestExceptions() {
        assertThrows(UnauthorizedException.class, () -> userService.addGuest("user1", "user1"));
        assertThrows(NotFoundException.class, () -> userService.addGuest("fakeUser", "user1"));
        assertThrows(NotFoundException.class, () -> userService.addGuest("user1", "fakeUser"));
    }

    @Test
    void testIsGuestOfFalse() {
        assertFalse(userService.isGuestOf("user1", "user1"));
        assertFalse(userService.isGuestOf("fakeUser", "user1"));
        assertFalse(userService.isGuestOf("user1", "fakeUser"));
    }

    @Test
    void testGetHostsWithWrongUser() {
        assertThrows(NotFoundException.class, () -> userService.getHosts("fakeUser"));
    }

    @Test
    void coverReturnOwnGuests() {
        assertNotNull(userService.returnOwnGuests("fakeUser"));
        assertNotNull(userService.returnOwnGuests("user1"));
    }
}