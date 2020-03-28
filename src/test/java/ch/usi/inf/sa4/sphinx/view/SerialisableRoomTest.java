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
        assertAll("Should return new Serialized user",
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


    @Nested
    @DisplayName("Testing constructor with Room as parameter")
    class SerialisableUserWithUser {

        @BeforeEach
        void createUser() {
            room = new Room();
        }

        @Test
        @DisplayName(" , where Room does not contain any device")
        void testUserConstructor() {
            room.setId(23);
            serialisableRoom = new SerialisableRoom(room);
            assertAll("Should return new SerializedRoom",
                    () -> assertEquals(serialisableRoom.name, room.getName()),
                    () -> assertEquals(serialisableRoom.icon, room.getIcon()),
                    () -> assertEquals(serialisableRoom.id, room.getId()),
                    () -> assertEquals(serialisableRoom.devices.length, room.getDevices().size()), //should be 0
                    () -> assertEquals(serialisableRoom.background, room.getBackground()));
        }

        @Test
        @DisplayName(" , where Room contains some devices")
        void testUserConstructorWithRooms() {
            int deviceNum = 15;
            for (int i = 1; i <= deviceNum; i++) {
                room.addDevice(i * 2);
            }
            serialisableRoom = new SerialisableRoom(room);
            for (int i = 0; i < deviceNum; i++) {
                assertEquals(serialisableRoom.devices[i], room.getDevices().get(i));
            }
        }
    }
}