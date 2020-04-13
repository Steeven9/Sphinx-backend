package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class VolatileDeviceStorageTest {

    @Autowired
    VolatileDeviceStorage deviceStorage;


    @Test
    @DisplayName("Test correct functionality of generateKey method")
    void testGenerateKey() {
        Integer id = deviceStorage.generateKey(new Light());
        Integer id1 = deviceStorage.generateKey(new MotionSensor());
        assertEquals(id + 1, id1);
        assertEquals(deviceStorage.data.size() + 1, id);
        assertEquals(deviceStorage.data.size() + 2, id1);
    }

    @Test
    void testStorageFunctionality_InsertingAndDeleting() {
        SmartPlug smartPlug = new SmartPlug();

        smartPlug.setName("testName3");
        smartPlug.setIcon("testIcon");
        Integer id = deviceStorage.insert(smartPlug);
        assertNotEquals(smartPlug, deviceStorage.get(id));//does not points to same object

        Device returnedRoom = deviceStorage.get(id);
        assertNotNull(returnedRoom);
        assertAll(
                () -> assertEquals("testIcon", returnedRoom.getIcon()),
                () -> assertEquals("testName3", returnedRoom.getName()),
                () -> assertEquals(id, returnedRoom.getId())
        );

        deviceStorage.delete(id);
        assertNull(deviceStorage.get(id));

        Device deviceWithLockedKey = new Light();
        deviceWithLockedKey.lockKey();
        assertNull(deviceStorage.insert(deviceWithLockedKey));
    }

    @Test
    @DisplayName("Test correct functionality of update method")
    void testUpdate() {
        TempSensor tempSensorNoKey = new TempSensor();
        HumiditySensor deviceWithNotExistingKey = new HumiditySensor();
        deviceWithNotExistingKey.setKey(222);
        assertFalse(deviceStorage.update(tempSensorNoKey));
        assertFalse(deviceStorage.update(deviceWithNotExistingKey));

        MotionSensor motionSensor = new MotionSensor();


        Integer id = deviceStorage.insert(motionSensor);
        assertEquals(motionSensor.getName(), deviceStorage.get(id).getName());

        String testName = "testNameForUpdate";
        motionSensor.setName(testName);

        assertTrue(deviceStorage.update(motionSensor));
        assertEquals(testName, deviceStorage.get(id).getName());
    }

}