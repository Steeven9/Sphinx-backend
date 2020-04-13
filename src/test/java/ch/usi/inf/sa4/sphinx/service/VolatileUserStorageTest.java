package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.Demo.DummyDataAdder;
import ch.usi.inf.sa4.sphinx.model.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class VolatileUserStorageTest {

    @Autowired
    VolatileUserStorage userStorage;
    @Autowired
    DummyDataAdder dummyDataAdder;

    @Test
    @Order(1)
    @DisplayName("Test that userStorage is initialized")
    void testInitializer() {
        assertEquals(0, userStorage.data.size());
    }

    @Test
    @Order(2)
    @DisplayName("Test correct functionality of generateKey method")
    void testGenerateKey() {
        String username = "emptyUser";
        dummyDataAdder.emptyUser();
        User user = userStorage.get(username);
        assertNotNull(user);
        assertEquals(username, userStorage.generateKey(user));

        User user1 = new User("unv@usi.ch", "1234", "unverifiedUser", "edeefefefef");
        assertEquals("unverifiedUser", userStorage.generateKey(user1));
    }

    @Test
    @Order(3)
    void testStorageFunctionality_InsertingAndDeleting() {
        String username = "randomUser";
        User user = new User("unv@usi.ch", "1234", "randomUser", "edeefefefef");

        String key = userStorage.insert(user);
        assertEquals(username, key);
        assertNotEquals(user, userStorage.get(key));//does not points to same object

        User user1 = userStorage.get(key);
        assertNotNull(user1);
        assertEquals(2, userStorage.data.size());

        userStorage.delete(key);
        assertNull(userStorage.get(key));

        User newUser = new User("mario@usi.ch", "1234", "user2", "mario rossi");
        newUser.lockKey();
        assertNull(userStorage.insert(newUser));
    }

    @Test
    @Order(4)
    @DisplayName("Test correct functionality of update method")
    void testUpdate() {

        User eventithNotExestingKey = new User("mario@usi.ch", "1234", "user1", "mario rossi");
        eventithNotExestingKey.setKey("TestSetKey");
        assertFalse(userStorage.update(eventithNotExestingKey));

        User newUser = new User("mario@usi.ch", "1234", "user3", "mario rossi");

        userStorage.insert(newUser);
        assertTrue(userStorage.update(newUser));
    }

    @Test
    @Order(5)
    @DisplayName("Test correct functionality of update getByEmail")
    void testGetByEmail() {
        String email = "testmail@usi.ch";
        User newUser = new User("testmail@usi.ch", "1234", "testUser", "mario rossi");
        assertNull(userStorage.getByEmail(email));
        assertEquals("testUser", userStorage.insert(newUser));
        User returnedUser = userStorage.getByEmail(email);
        assertNotEquals(newUser, returnedUser);
        assertAll("check if two users are the same users",
                () -> assertEquals(newUser.getEmail(), returnedUser.getEmail()),
                () -> assertEquals(newUser.getUsername(), returnedUser.getUsername()),
                () -> assertEquals(newUser.getFullname(), returnedUser.getFullname()),
                () -> assertEquals(newUser.getPassword(), returnedUser.getPassword()),
                () -> assertFalse(newUser.isVerified()));
    }
}