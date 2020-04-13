package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.Coupling;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class VolatileCouplingStorageTest {

    @Autowired
    VolatileCouplingStorage couplingStorage;

    @Test
    @Order(1)
    @DisplayName("Test that couplingStorage is initialized")
    void testInitializer() {
        Map<Integer, Coupling> data = couplingStorage.data;
        assertEquals(0, data.size());
    }

    @Test
    @Order(2)
    @DisplayName("Test correct functionality of generateKey method")
    void testGenerateKey() {
        Integer id = couplingStorage.generateKey(new Coupling(1, 13));
        assertEquals(1, id);
        id = couplingStorage.generateKey(new Coupling(2, 12));
        assertEquals(2, id);
    }

    @Test
    @Order(3)
    void testStorageFunctionality_InsertingAndDeleting() {
        Integer id = 3;

        Coupling coupling = new Coupling(id, 22);
        assertNull(couplingStorage.get(id));

        Integer id3 = couplingStorage.insert(coupling);
        assertEquals(id, id3);
        assertNotEquals(coupling, couplingStorage.get(id3));//does not points to same object

        Coupling coupling1 = couplingStorage.get(id);
        assertNotNull(coupling1);
        assertEquals(1, couplingStorage.data.size());

        couplingStorage.delete(id3);
        assertNull(couplingStorage.get(id3));

        Coupling couplingWithoutKey = new Coupling(3, 4);
        couplingWithoutKey.lockKey();
        assertNull(couplingStorage.insert(couplingWithoutKey));
    }

    @Test
    @Order(4)
    @DisplayName("Test correct functionality of update method")
    void testUpdate() {
        Coupling couplingNoKey = new Coupling(1, 3);
        Coupling couplingWithNotExestingKey = new Coupling(2, 4);


        couplingWithNotExestingKey.setKey(222);
        assertFalse(couplingStorage.update(couplingNoKey));
        assertFalse(couplingStorage.update(couplingWithNotExestingKey));

        Coupling coupling = new Coupling(3, 4);

        couplingStorage.insert(coupling);
        assertTrue(couplingStorage.update(coupling));
    }
}