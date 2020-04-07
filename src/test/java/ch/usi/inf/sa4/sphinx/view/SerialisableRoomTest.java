package ch.usi.inf.sa4.sphinx.view;

import ch.usi.inf.sa4.sphinx.model.Room;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class SerialisableRoomTest {
    SerialisableRoom serialisableRoom;
    Room room;

    static Stream<Arguments> argumentsProvider() {
        return Stream.of(
                arguments(101, "testName", "testIcon", "testBackground", new Integer[]{3, 6}),
                arguments(0, " ", "icon2", "@Back", new Integer[]{3, -6, 0}),
                arguments(-1, "NONAME", "", "grey", new Integer[]{333333, 0, 6 + 8})
        );
    }

    @ParameterizedTest
    @MethodSource("argumentsProvider")
    @DisplayName("Testing constructor with multiple arguments")
    void testLongConstructor(Integer id, String name, String icon, String background, Integer[] devices) {
        serialisableRoom = new SerialisableRoom(id, name, icon, background, devices);


        assertAll("Should return new SerializedRoom",

                () -> assertEquals(id, serialisableRoom.id),
                () -> assertEquals(icon, serialisableRoom.icon),
                () -> assertEquals(background, serialisableRoom.background),
                () -> assertEquals(devices, serialisableRoom.devices),
                () -> assertEquals(name, serialisableRoom.name));
    }

    @Test
    @DisplayName("Testing the creation of constructor without parameters")
    void existSerializedRoom() {
        SerialisableRoom room = new SerialisableRoom();
        assertNotNull(room);
    }

}