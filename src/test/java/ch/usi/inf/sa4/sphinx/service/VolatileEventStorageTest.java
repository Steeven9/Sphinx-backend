package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.Event;
import ch.usi.inf.sa4.sphinx.model.StatelessDimmSwitchChangedEvent;
import ch.usi.inf.sa4.sphinx.model.DimmSwitchChangedEvent;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class VolatileEventStorageTest {

    @Autowired
    VolatileEventStorage eventStorage;


    @Test
    @DisplayName("Test correct functionality of generateKey method")
    void testGenerateKey() {
        Integer id = eventStorage.generateKey(new StatelessDimmSwitchChangedEvent(1, 1));
        Integer id1 = eventStorage.generateKey(new StatelessDimmSwitchChangedEvent(2, 1));
        assertEquals(id + 1, id1);
        assertEquals(eventStorage.data.size() + 1, id);
        assertEquals(eventStorage.data.size() + 2, id1);
    }

    @Test
    void testStorageFunctionality_InsertingAndDeleting() {

        DimmSwitchChangedEvent event1 = new DimmSwitchChangedEvent(34);

        Integer id = eventStorage.insert(event1);
        assertNotEquals(event1, eventStorage.get(id));//does not points to same object

        Event<?> sameEvent = eventStorage.get(id);
        assertNotNull(sameEvent);
        assertEquals(34, sameEvent.getDeviceId());

        eventStorage.delete(id);
        assertNull(eventStorage.get(id));

        DimmSwitchChangedEvent eventWithoutKey = new DimmSwitchChangedEvent(id);
        eventWithoutKey.lockKey();
        assertNull(eventStorage.insert(eventWithoutKey));
    }

    @Test
    @DisplayName("Test correct functionality of update method")
    void testUpdate() {
        StatelessDimmSwitchChangedEvent eventNoKey = new StatelessDimmSwitchChangedEvent(1, 21);
        assertFalse(eventStorage.update(eventNoKey));

        DimmSwitchChangedEvent event = new DimmSwitchChangedEvent(3);
        eventStorage.insert(event);
        assertTrue(eventStorage.update(event));
    }

}