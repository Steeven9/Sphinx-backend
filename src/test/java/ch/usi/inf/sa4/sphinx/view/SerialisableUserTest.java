package ch.usi.inf.sa4.sphinx.view;

import ch.usi.inf.sa4.sphinx.model.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class SerialisableUserTest {
    SerialisableUser serialisableUser;
    User user;

    static Stream<Arguments> argumentsProvider() {
        return Stream.of(
                arguments("testUsername", "testEmail", "testFullanme", "testPassword", new Integer[]{3, 6}),
                arguments("__demonsRise", "007@mail.com", "Pachovski", "@keyPASS", new Integer[]{3, -6, 0}),
                arguments("", "fake@mail.com", " ", "1234", new Integer[]{333333, 0, 6 + 8})
        );
    }

    @ParameterizedTest
    @MethodSource("argumentsProvider")
    @DisplayName("Testing constructor with multiple arguments")
    void testLongConstructor(String username, String email, String fullname, String password, Integer[] rooms) {
        serialisableUser = new SerialisableUser(username, email, fullname, password, rooms);
        assertAll("Should return new Serialized user",
                () -> assertEquals(username, serialisableUser.username),
                () -> assertEquals(email, serialisableUser.email),
                () -> assertEquals(fullname, serialisableUser.fullname),
                () -> assertEquals(password, serialisableUser.password),
                () -> assertEquals(rooms, serialisableUser.rooms));
    }

    @Test
    @DisplayName("Testing creation of constructor without parameters")
    void existSerializedUser() {
        SerialisableUser serialisableUser = new SerialisableUser();
        assertNotNull(serialisableUser);
    }
}