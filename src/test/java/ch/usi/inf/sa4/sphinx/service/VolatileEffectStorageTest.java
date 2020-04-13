package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class VolatileEffectStorageTest {

    @Autowired
    VolatileEffectStorage effectStorage;

    @Test
    @Order(1)
    @DisplayName("Test that effectStorage is initialized")
    void testInitializer() {
        Map<Integer, Effect<?>> data = effectStorage.data;
        assertEquals(0, data.size());
    }

    @Test
    @Order(2)
    @DisplayName("Test correct functionality of generateKey method")
    void testGenerateKey() {
        Integer id = effectStorage.generateKey(new DimmableLightStateInc(1));
        assertEquals(1, id);
        id = effectStorage.generateKey(new DimmableLightStateInc(2));
        assertEquals(2, id);
    }


    @Test
    @Order(3)
    void testStorageFunctionality_InsertingAndDeleting() {
        Integer id = 3;

        DimmableLightStateInc effect = new DimmableLightStateInc(id);
        assertNull(effectStorage.get(id));

        Integer id3 = effectStorage.insert(effect);
        assertEquals(id, id3);
        assertNotEquals(effect, effectStorage.get(id3));//does not points to same object

        Effect<?> effect1 = effectStorage.get(id);
        assertNotNull(effect1);
        assertEquals(1, effectStorage.data.size());

        effectStorage.delete(id3);
        assertNull(effectStorage.get(id3));

        DimmableLightStateSet effectWithoutKey = new DimmableLightStateSet(id);
        effectWithoutKey.lockKey();
        assertNull(effectStorage.insert(effectWithoutKey));
    }

    @Test
    @Order(4)
    @DisplayName("Test correct functionality of update method")
    void testUpdate() {
        DimmableLightStateSet effectNoKey = new DimmableLightStateSet(1);
        DimmableLightStateInc effectithNotExestingKey = new DimmableLightStateInc(2);


        effectithNotExestingKey.setKey(222);
        assertFalse(effectStorage.update(effectNoKey));
        assertFalse(effectStorage.update(effectithNotExestingKey));

        DimmableLightStateSet effect = new DimmableLightStateSet(3);

        effectStorage.insert(effect);
        assertTrue(effectStorage.update(effect));
    }
}