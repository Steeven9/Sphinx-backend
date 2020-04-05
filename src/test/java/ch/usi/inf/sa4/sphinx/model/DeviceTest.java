package ch.usi.inf.sa4.sphinx.model;
import ch.usi.inf.sa4.sphinx.view.SerialisableDevice;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DeviceTest {

    @Test
    void shouldReturnTrueIfCronstructorCreatesNotNull() {
        Device d = new Dimmable();
        assertNotNull(d);
    }

    @Test
    void shouldReturnTrueIfSettingIdIsNotSetBefore() {
        Device d = new DimmableSwitch();
        assertTrue(d.setId(1));
        assertEquals(1, d.getId());
    }

    @Test
    void shouldReturnFalseIfSettingIdIsSetBefore() {
        Device c = new Light();
        c.setId(1);
        assertFalse(c.setId(4283));
    }


    @Test
    void shouldSetIcon() {
        Device d = new Switch();
        d.setIcon("TEST_ICON");
        assertEquals("TEST_ICON", d.getIcon());
    }

    @Test
    void shouldSetName() {
        Device d = new MotionSensor();
        d.setName("TEST_NAME");
        assertEquals("TEST_NAME", d.getName());
    }

    @Test
    void shouldReturnTrueIfIsOn() {
        Device d = new DimmableLight();
        d.setOn(true);
        assertTrue(d.isOn());
    }

    @Test
    void shouldReturnFalseIfIsOff() {
        Device d = new HumiditySensor();
        d.setOn(false);
        assertFalse(d.isOn());
    }

    @Test
    void shouldReturnSerialisableVersion() {
        Device d = new LightSensor();
        d.setOn(false);
        d.setName("TEST_NAME");
        d.setId(23);
        SerialisableDevice sd = d.serialise();
        assertEquals("TEST_NAME", sd.name);
        assertEquals(23, sd.id);
        assertFalse(d.isOn());
    }

    @Test
    void shouldAddObserver() {
        Device d = new SmartPlug();
        Integer observer = 10;
        d.addObserver(observer);
        assertTrue(d.couplings.contains(observer));
    }

    @Test
    void shouldSetANewCopiedDeviceUsingConstructor() {
        Switch d = new Switch();
        d.setId(23);
        Device sd = new Switch(d);
        d.setId(23);
        assertEquals("TEST_NAME", sd.getName());
        assertEquals(23, sd.getId());
        assertFalse(d.isOn());
    }
}