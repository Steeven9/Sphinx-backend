package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.Room;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class VolatileRoomStorageTest {

    @Autowired
    VolatileRoomStorage roomStorage;


    @Test
    @Order(1)
    @DisplayName("Test that roomStorage is initialized")
    void testInitializer() {
        Map<Integer, Room> data = roomStorage.data;
        assertEquals(0, data.size());
    }

    @Test
    @Order(2)
    @DisplayName("Test correct functionality of generateKey method")
    void testGenerateKey() {
        Integer id = roomStorage.generateKey(new Room());
        assertEquals(1, id);
        id = roomStorage.generateKey(new Room());
        assertEquals(2, id);
    }

    @Test
    @Order(3)
    void testStorageFunctionality_InsertingAndDeleting() {
        Integer id = 3;
        Room room1 = new Room();
        room1.setName("testRoom3");
        room1.setBackground("testBackground3");

        assertNull(roomStorage.get(id));
        Integer id3 = roomStorage.insert(room1);
        assertEquals(id, id3);
        assertNotEquals(room1, roomStorage.get(id3));//does not points to same object

        Room returnedRoom = roomStorage.get(id);
        assertNotNull(returnedRoom);
        assertAll(
                () -> assertEquals("testBackground3", returnedRoom.getBackground()),
                () -> assertEquals("testRoom3", returnedRoom.getName()),
                () -> assertEquals(1, roomStorage.data.size())
        );

        roomStorage.delete(id3);
        assertNull(roomStorage.get(id3));

        Room roomWithLockedKey = new Room();
        roomWithLockedKey.lockKey();
        assertNull(roomStorage.insert(roomWithLockedKey));
    }

    @Test
    @Order(4)
    @DisplayName("Test correct functionality of update method")
    void testUpdate() {
        Room roomWithoutKey = new Room();
        Room roomWithNotExestingKey = new Room();
        roomWithNotExestingKey.setKey(222);
        assertFalse(roomStorage.update(roomWithoutKey));
        assertFalse(roomStorage.update(roomWithNotExestingKey));

        Room room = new Room();

        Integer id = roomStorage.insert(room);
        assertEquals(room.getName(), roomStorage.get(id).getName());

        String testName = "testNameForUpdate";
        room.setName(testName);

        assertTrue(roomStorage.update(room));
        assertEquals(testName, roomStorage.get(id).getName());
    }
}