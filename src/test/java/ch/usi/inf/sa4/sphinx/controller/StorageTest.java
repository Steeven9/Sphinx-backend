package ch.usi.inf.sa4.sphinx.controller;

import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.Light;
import ch.usi.inf.sa4.sphinx.model.Room;
import ch.usi.inf.sa4.sphinx.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StorageTest {

    @Test
    void getNonexistingUserShouldReturnNull() {
        assertNull(Storage.getUser("test"));
    }

    @Test
    void getNonexistingRoomShouldReturnNull() {
        assertNull(Storage.getRoom(0));
    }

    @Test
    void getNonexistingDeviceShouldReturnNull() {
        assertNull(Storage.getDevice(0));
    }

    @Test
    void insertedUserShouldBeGettable() {
        User test = new User("test"); //TODO: use actual constructor once it exists
        Storage.insertUser(test);
        assertEquals(test, Storage.getUser("test"));
    }

    @Test
    void insertedRoomShouldBeGettable() {
        Room test = new Room(1); //TODO: use actual constructor once it exists
        Storage.insertRoom(test);
        assertEquals(test, Storage.getRoom(1));
    }

    @Test
    void insertedDeviceShouldBeGettable() {
        Device test = new Light(1); //TODO: use actual constructor once it exists
        Storage.insertDevice(test);
        assertEquals(test, Storage.getDevice(1));
    }

    @Test
    void deletedUserShouldNotBeGettable() {
        User test = new User("test2"); //TODO: use actual constructor once it exists
        Storage.insertUser(test);
        assertEquals(test, Storage.getUser("test2"));
        Storage.deleteUser("test2");
        assertNull(Storage.getUser("test2"));
    }

    @Test
    void deletedRoomShouldNotBeGettable() {
        Room test = new Room(2); //TODO: use actual constructor once it exists
        Storage.insertRoom(test);
        assertEquals(test, Storage.getRoom(2));
        Storage.deleteRoom(2);
        assertNull(Storage.getRoom(2));
    }

    @Test
    void deletedDeviceShouldNotBeGettable() {
        Device test = new Light(2); //TODO: use actual constructor once it exists
        Storage.insertDevice(test);
        assertEquals(test, Storage.getDevice(2));
        Storage.deleteDevice(2);
        assertNull(Storage.getDevice(2));
    }

    @Test
    void insertedUserShouldBeGettableByEmail() {
        User test = new User("test3", "test3@email.io"); //TODO: use actual constructor once it exists
        Storage.insertUser(test);
        assertEquals(test, Storage.getUserByEmail("test3@email.io"));
    }

    @Test
    void shouldNotInsertDuplicateEmail() {
        User test = new User("test4", "test4@email.io"); //TODO: use actual constructor once it exists
        Storage.insertUser(test);
        assertEquals(test, Storage.getUser("test4"));
        assertEquals(test, Storage.getUserByEmail("test4@email.io"));

        User test2 = new User("invalidtest", "test4@email.io"); //TODO: use actual constructor once it exists
        Storage.insertUser(test2);
        assertNull(Storage.getUser("invalidtest"));
        assertEquals(test, Storage.getUserByEmail("test4@email.io"));
    }

    @Test
    void shouldNotInsertDuplicateUsername() {
        User test = new User("test5", "test5@email.io"); //TODO: use actual constructor once it exists
        Storage.insertUser(test);
        assertEquals(test, Storage.getUser("test5"));
        assertEquals(test, Storage.getUserByEmail("test5@email.io"));

        User test2 = new User("test5", "invalidtest@email.io"); //TODO: use actual constructor once it exists
        Storage.insertUser(test2);
        assertNull(Storage.getUserByEmail("invalidtest@email.io"));
        assertEquals(test, Storage.getUser("test5"));
    }

    @Test
    void deleteShouldFreeUsername() {
        User test = new User("test6", "test6@email.io"); //TODO: use actual constructor once it exists
        Storage.insertUser(test);
        assertEquals(test, Storage.getUser("test6"));
        assertEquals(test, Storage.getUserByEmail("test6@email.io"));

        Storage.deleteUser("test6");
        User test2 = new User("test6", "newtest@email.io"); //TODO: use actual constructor once it exists
        Storage.insertUser(test2);
        assertEquals(test2, Storage.getUser("test6"));
        assertEquals(test2, Storage.getUserByEmail("newtest@email.io"));
    }

    @Test
    void deleteShouldFreeEmail() {
        User test = new User("test7", "test7@email.io"); //TODO: use actual constructor once it exists
        Storage.insertUser(test);
        assertEquals(test, Storage.getUser("test7"));
        assertEquals(test, Storage.getUserByEmail("test7@email.io"));

        Storage.deleteUser("test7");
        User test2 = new User("newtest", "test7@email.io"); //TODO: use actual constructor once it exists
        Storage.insertUser(test2);
        assertEquals(test2, Storage.getUser("newtest"));
        assertEquals(test2, Storage.getUserByEmail("test7@email.io"));
    }
}