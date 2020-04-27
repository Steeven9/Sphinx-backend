package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.Light;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DeviceServiceTest {

    @Autowired
    DeviceService deviceService;
    @Autowired
    VolatileDeviceStorage deviceStorage;
    @Autowired
    RoomService roomService;
    @Autowired
    CouplingService couplingService;

    @Test
    @DisplayName("Tests both get and update methods")
    void testDeviceService() {
        assertNull(deviceService.get(9999));

        Light device = new Light(roomService, couplingService);
        device.setName("testName");
        Integer id = deviceStorage.insert(device);
        Device returnedDevice = deviceService.get(id);
        assertNotEquals(device, returnedDevice); //does not point to the same object
        assertEquals("testName", returnedDevice.getName());

        String name = "secondTestName";//test update method
        device.setName(name);

        assertTrue(deviceService.update(device));
        returnedDevice = deviceService.get(id);
        assertEquals(name, returnedDevice.getName());

        assertFalse(deviceService.update(new Light(roomService, couplingService)));
    }

}