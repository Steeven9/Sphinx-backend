package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class VolatileDeviceStorageTest {

    @Autowired
    VolatileDeviceStorage deviceStorage;
    @Autowired
    RoomService roomService;
    @Autowired
    CouplingService couplingService;


    @Test
    void testStorageFunctionality_InsertingAndDeleting() {
        SmartPlug smartPlug = new SmartPlug(roomService, couplingService);

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

        Device deviceWithLockedKey = new Light(roomService, couplingService);
        deviceWithLockedKey.lockKey();
        assertNull(deviceStorage.insert(deviceWithLockedKey));
    }

    @Test
    @DisplayName("Test correct functionality of update method")
    void testUpdate() {
        TempSensor tempSensorNoKey = new TempSensor(roomService, couplingService);
        HumiditySensor deviceWithNotExistingKey = new HumiditySensor(roomService, couplingService);
        deviceWithNotExistingKey.setKey(222);
        assertFalse(deviceStorage.update(tempSensorNoKey));
        assertFalse(deviceStorage.update(deviceWithNotExistingKey));

        MotionSensor motionSensor = new MotionSensor(roomService, couplingService);


        Integer id = deviceStorage.insert(motionSensor);
        assertEquals(motionSensor.getName(), deviceStorage.get(id).getName());

        String testName = "testNameForUpdate";
        motionSensor.setName(testName);

        assertTrue(deviceStorage.update(motionSensor));
        assertEquals(testName, deviceStorage.get(id).getName());
    }

}