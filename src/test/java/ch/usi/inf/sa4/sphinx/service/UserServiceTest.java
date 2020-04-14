package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.Demo.DummyDataAdder;
import ch.usi.inf.sa4.sphinx.model.Room;
import ch.usi.inf.sa4.sphinx.model.User;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;
    @Autowired
    DummyDataAdder dataAdder;

    @Test
    @DisplayName("test different getters, deletion, updating and inserting methods")
    void testUser() {
        String username = "serviceUsername";
        String email = "casual@usi.ch";
        User newUser = new User(email, "1234", username, "mario rossi");
        assertAll("test with not existing data",
                () ->  assertNull(userService.get("notExistingUser")),
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
    void testDevices() {
        String username = "serviceUsername3";
        String email = "casual3@usi.ch";
        User newUser = new User(email, "1234", username, "mario rossi");
        Room room = new Room();
        room.setName("testName");


    }
}