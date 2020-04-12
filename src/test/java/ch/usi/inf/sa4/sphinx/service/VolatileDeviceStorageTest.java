package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class VolatileDeviceStorageTest {

    @Autowired
    VolatileDeviceStorage deviceStorage;

    @Test
    @Order(1)
    @DisplayName("Test that deviceStorage is initialized")
    void testInitializer() {
        Map<Integer, Device> data = deviceStorage.data;
        assertEquals(0, data.size());
    }

    @Test
    @Order(2)
    @DisplayName("Test correct functionality of generateKey method")
    void testGenerateKey() {
        Integer id = deviceStorage.generateKey(new Light());
        assertEquals(1, id);
        id = deviceStorage.generateKey(new MotionSensor());
        assertEquals(2, id);
    }

    @Test
    @Order(3)
    void testStorageFunctionality_InsertingAndDeleting() {
        Integer id = 3;
        SmartPlug smartPlug = new SmartPlug();

        assertNull(deviceStorage.get(id));
        smartPlug.setName("testName3");
        smartPlug.setIcon("testIcon");
        Integer id3 = deviceStorage.insert(smartPlug);
        assertEquals(id, id3);
        assertNotEquals(smartPlug, deviceStorage.get(id3));//does not points to same object

        Device returnedRoom = deviceStorage.get(id);
        assertNotNull(returnedRoom);
        assertAll(
                () -> assertEquals("testIcon", returnedRoom.getIcon()),
                () -> assertEquals("testName3", returnedRoom.getName()),
                () -> assertEquals(1, deviceStorage.data.size())
        );

        deviceStorage.delete(id3);
        assertNull(deviceStorage.get(id3));

        Device deviceWithLockedKey = new Light();
        deviceWithLockedKey.lockKey();
        assertNull(deviceStorage.insert(deviceWithLockedKey));
    }

    @Test
    @Order(4)
    @DisplayName("Test correct functionality of update method")
    void testUpdate() {
        TempSensor tempSensorNoKey = new TempSensor();
        HumiditySensor deviceWithNotExestingKey = new HumiditySensor();
        deviceWithNotExestingKey.setKey(222);
        assertFalse(deviceStorage.update(tempSensorNoKey));
        assertFalse(deviceStorage.update(deviceWithNotExestingKey));

        MotionSensor motionSensor = new MotionSensor();


        Integer id = deviceStorage.insert(motionSensor);
        assertEquals(motionSensor.getName(), deviceStorage.get(id).getName());

        String testName = "testNameForUpdate";
        motionSensor.setName(testName);

        assertTrue(deviceStorage.update(motionSensor));
        assertEquals(testName, deviceStorage.get(id).getName());
    }

}