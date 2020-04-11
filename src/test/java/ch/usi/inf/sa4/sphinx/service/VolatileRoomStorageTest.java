package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.Room;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class VolatileRoomStorageTest {

    @Autowired
    VolatileRoomStorage roomStorage;

    static Stream<Arguments> argumentsProvider() {
        Room room1 = new Room();
        Room room2 = new Room();
        Room room3 = new Room();
        room1.setName("testRoom1");
        room3.setName("testRoom3");
        room1.setBackground("testBackground1");
        room3.setBackground("testBackground3");
        return Stream.of(
                arguments(room1, room1.getName(), room1.getBackground()),
                arguments(room2, room1.getName(), room1.getBackground()),
                arguments(room3, room1.getName(), room1.getBackground())
        );
    }

    @Test
    @Order(1)
    void testInitializer() {
        Map<Integer, Room> data = roomStorage.data;
        assertEquals(0, data.size());
    }

    @Test
    @Order(2)
    void testGenerateKey() {
        Integer id = roomStorage.generateKey(new Room());
        assertEquals(1, id);
        id = roomStorage.generateKey(new Room());
        assertEquals(2, id);
    }

    @Test
    @Order(3)
    void testStorageFunctionality_InsertingOneNewRoom() {
        Integer id = 3;
        assertNull(roomStorage.get(id));
        Room room1 = new Room();
        room1.setName("testRoom3");
        room1.setBackground("testBackground3");
        Integer id1 = roomStorage.insert(room1);
        assertEquals(id, id1);
        Room returnedRoom = roomStorage.get(id);
        assertNotNull(returnedRoom);
        assertAll(
                () -> assertEquals("testBackground3", returnedRoom.getBackground()),
                () -> assertEquals("testRoom3", returnedRoom.getName()),
                () -> assertEquals(3, roomStorage.data.size())
        );
    }
}