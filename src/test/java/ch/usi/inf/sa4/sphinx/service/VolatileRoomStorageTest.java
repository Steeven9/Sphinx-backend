package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.Room;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class VolatileRoomStorageTest {

//    @Autowired
//    VolatileRoomStorage roomStorage;
//
//
//    @Test
//    void testStorageFunctionality_InsertingAndDeleting() {
//        Room room1 = new Room();
//        room1.setName("testRoom3");
//        room1.setBackground("testBackground3");
//
//        Integer id = roomStorage.insert(room1);
//        assertNotEquals(room1, roomStorage.get(id));//does not points to same object
//
//        Room returnedRoom = roomStorage.get(id);
//        assertNotNull(returnedRoom);
//        assertAll(
//                () -> assertEquals("testBackground3", returnedRoom.getBackground()),
//                () -> assertEquals("testRoom3", returnedRoom.getName()),
//                () -> assertEquals(id, returnedRoom.getId())
//
//        );
//
//        roomStorage.delete(id);
//        assertNull(roomStorage.get(id));
//
//        Room roomWithLockedKey = new Room();
//        roomWithLockedKey.lockKey();
//        assertNull(roomStorage.insert(roomWithLockedKey));
//    }
//
//    @Test
//    @DisplayName("Test correct functionality of update method")
//    void testUpdate() {
//        Room roomWithoutKey = new Room();
//        Room roomWithNotExestingKey = new Room();
//        roomWithNotExestingKey.setKey(222);
//        assertFalse(roomStorage.update(roomWithoutKey));
//        assertFalse(roomStorage.update(roomWithNotExestingKey));
//
//        Room room = new Room();
//
//        Integer id = roomStorage.insert(room);
//        assertEquals(room.getName(), roomStorage.get(id).getName());
//
//        String testName = "testNameForUpdate";
//        room.setName(testName);
//
//        assertTrue(roomStorage.update(room));
//        assertEquals(testName, roomStorage.get(id).getName());
//    }
}