package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.Demo.DummyDataAdder;
import ch.usi.inf.sa4.sphinx.model.User;
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
    @DisplayName("test different getters, deletion and inserting methods")
    void testUserGetterAndSetter() {
        String username = "serviceUsername";
        String email = "casual@usi.ch";
        User newUser = new User("casual@usi.ch", "1234", username, "mario rossi");
        assertAll("test with not existing data",
                () ->  assertNull(userService.get("notExistingUser")),
                () -> assertNull(userService.get(username)),
                () -> assertNull(userService.getByMail(email)),
                () -> assertNull(userService.getByMail("notExisting@usi.ch"))
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
                () -> assertEquals("mario rossi", returnedUserByEmail.getFullname())
        );
        userService.delete(username);
        assertNull(userService.get(username));
    }
}