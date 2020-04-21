package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.Coupling;
import ch.usi.inf.sa4.sphinx.model.StatelessDimmSwitchChangedEvent;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class VolatileCouplingStorageTest {

//    @Autowired
//    VolatileCouplingStorage couplingStorage;
//
//
//    @Test
//    void testStorageFunctionality_InsertingAndDeleting() {
//
//        Coupling coupling = new Coupling(3, 22);
//
//        Integer id = couplingStorage.insert(coupling);
//        assertNotEquals(coupling, couplingStorage.get(id));//does not points to same object
//
//        Coupling coupling1 = couplingStorage.get(id);
//        assertNotNull(coupling1);
//        assertEquals(id, coupling.getId());
//
//        couplingStorage.delete(id);
//        assertNull(couplingStorage.get(id));
//
//        Coupling couplingWithLockedKey = new Coupling(3, 4);
//        couplingWithLockedKey.lockKey();
//        assertNull(couplingStorage.insert(couplingWithLockedKey));
//    }
//
//    @Test
//    @DisplayName("Test correct functionality of update method")
//    void testUpdate() {
//        Coupling couplingNoKey = new Coupling(1, 3);
//        Coupling couplingWithNotExestingKey = new Coupling(2, 4);
//
//
//        couplingWithNotExestingKey.setKey(222);
//        assertFalse(couplingStorage.update(couplingNoKey));
//        assertFalse(couplingStorage.update(couplingWithNotExestingKey));
//
//        Coupling coupling = new Coupling(3, 4);
//
//        couplingStorage.insert(coupling);
//        assertTrue(couplingStorage.update(coupling));
//    }
}