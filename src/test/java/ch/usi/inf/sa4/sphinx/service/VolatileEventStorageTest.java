package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.Event;
import ch.usi.inf.sa4.sphinx.model.StatelessDimmSwitchChangedEvent;
import ch.usi.inf.sa4.sphinx.model.DimmSwitchChangedEvent;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class VolatileEventStorageTest {

    @Autowired
    VolatileEventStorage eventStorage;


    @Test
    @Order(1)
    @DisplayName("Test that eventStorage is initialized")
    void testInitializer() {
        Map<Integer, Event<?>> data = eventStorage.data;
        assertEquals(0, data.size());
    }

    @Test
    @Order(2)
    @DisplayName("Test correct functionality of generateKey method")
    void testGenerateKey() {
        Integer id = eventStorage.generateKey(new StatelessDimmSwitchChangedEvent(1, 1));
        assertEquals(1, id);
        id = eventStorage.generateKey(new StatelessDimmSwitchChangedEvent(2, 1));
        assertEquals(2, id);
    }

    @Test
    @Order(3)
    void testStorageFunctionality_InsertingAndDeleting() {
        Integer id = 3;

        DimmSwitchChangedEvent event1 = new DimmSwitchChangedEvent(id);
        assertNull(eventStorage.get(id));

        Integer id3 = eventStorage.insert(event1);
        assertEquals(id, id3);
        assertNotEquals(event1, eventStorage.get(id3));//does not points to same object

        Event<?> event = eventStorage.get(id);
        assertNotNull(event);
        assertEquals(1, eventStorage.data.size());

        eventStorage.delete(id3);
        assertNull(eventStorage.get(id3));

        DimmSwitchChangedEvent eventWithoutKey = new DimmSwitchChangedEvent(id);
        eventWithoutKey.lockKey();
        assertNull(eventStorage.insert(eventWithoutKey));
    }

    @Test
    @Order(4)
    @DisplayName("Test correct functionality of update method")
    void testUpdate() {
        StatelessDimmSwitchChangedEvent eventNoKey = new StatelessDimmSwitchChangedEvent(1, 21);
        DimmSwitchChangedEvent eventithNotExestingKey = new DimmSwitchChangedEvent(2);


        eventithNotExestingKey.setKey(222);
        assertFalse(eventStorage.update(eventNoKey));
        assertFalse(eventStorage.update(eventithNotExestingKey));

        DimmSwitchChangedEvent event = new DimmSwitchChangedEvent(3);

        eventStorage.insert(event);
        assertTrue(eventStorage.update(event));
    }

}