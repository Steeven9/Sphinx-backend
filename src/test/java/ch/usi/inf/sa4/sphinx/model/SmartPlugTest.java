package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.service.CouplingService;
import ch.usi.inf.sa4.sphinx.service.RoomService;
import ch.usi.inf.sa4.sphinx.view.SerialisableDevice;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SmartPlugTest {

    @Autowired
    RoomService roomService;
    @Autowired
    CouplingService couplingService;

    @Test
    void shouldReturnTrueIfCronstructorCreatesNotNull() {
        SmartPlug d = new SmartPlug(roomService, couplingService);
        assertNotNull(d);
    }

    @Test
    void shouldReturnTrueIfSettingIdIsNotSetBefore() {
        SmartPlug d = new SmartPlug(roomService, couplingService);
        assertTrue(d.setId(1));
    }

    @Test
    void shouldReturn10IfReset() {
        SmartPlug d = new SmartPlug(roomService, couplingService);
        assertEquals(10.0, d.getPowerUsed());
        assertEquals(20.0, d.getPowerUsed());
        d.reset();
        assertEquals(10, d.getPowerUsed());
    }

    @Test
    void shouldSetANewCopiedSmartPlugUsingMakeCopy() {
        SmartPlug d = new SmartPlug(roomService, couplingService);
        d.setName("TEST_NAME");
        d.setId(40);
        SmartPlug sd = d.makeCopy();
        assertEquals("TEST_NAME", sd.getName());
        assertEquals("20.0 kWh", sd.getLabel());
        assertEquals(40, sd.getId());
        assertTrue(d.isOn());
    }

    @Test
    void shouldSetANewCopiedDeviceUsingConstructor() {
        SmartPlug d = new SmartPlug(roomService, couplingService);
        d.setOn(false);
        d.setName("TEST_NAME");
        d.setId(23);
        SmartPlug sd = new SmartPlug(d);
        assertEquals("TEST_NAME", sd.getName());
        assertEquals("20.0 kWh",sd.getLabel());
        assertEquals(23, sd.getId());
        assertFalse(d.isOn());

    }
}