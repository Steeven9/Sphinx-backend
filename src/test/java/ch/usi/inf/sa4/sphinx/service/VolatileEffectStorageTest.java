package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class VolatileEffectStorageTest {

//    @Autowired
//    VolatileEffectStorage effectStorage;
//
//
//    @Test
//    void testStorageFunctionality_InsertingAndDeleting() {
//        DimmableLightStateInc effect = new DimmableLightStateInc(3);
//
//        Integer id = effectStorage.insert(effect);
//        assertNotEquals(effect, effectStorage.get(id));//does not points to same object
//
//        Effect<?> effect1 = effectStorage.get(id);
//        assertNotNull(effect1);
//        assertEquals(id, effect1.getKey());
//
//        effectStorage.delete(id);
//        assertNull(effectStorage.get(id));
//
//        DimmableLightStateSet effectWithoutKey = new DimmableLightStateSet(5);
//        effectWithoutKey.lockKey();
//        assertNull(effectStorage.insert(effectWithoutKey));
//    }
//
//    @Test
//    @DisplayName("Test correct functionality of update method")
//    void testUpdate() {
//        DimmableLightStateSet effectNoKey = new DimmableLightStateSet(1);
//        DimmableLightStateInc effectWithNotExistingKey = new DimmableLightStateInc(2);
//
//        //does not exist in the storage
//        effectWithNotExistingKey.setKey(222);
//        assertFalse(effectStorage.update(effectNoKey));
//        assertFalse(effectStorage.update(effectWithNotExistingKey));
//
//        DimmableLightStateSet effect = new DimmableLightStateSet(3);
//
//        effectStorage.insert(effect);
//        assertTrue(effectStorage.update(effect));
//    }
}