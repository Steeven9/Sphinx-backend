package ch.usi.inf.sa4.sphinx.model;
import ch.usi.inf.sa4.sphinx.view.SerialisableDevice;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeviceTest {

    @Test
    void shouldReturnTrueIfConstructorCreatesNotNull() {
        Device d = new Dimmable();
        assertNotNull(d);
    }


    @Test
    void shouldReturnFalseIfSetKetCannotBeSet() {
        Device d = new Dimmable();
        d.setKey(3);
        d.lockKey();
        assertFalse(d.setKey(1));
    }

    @Test
    void shouldReturnTrueIfSettingIdIsNotSetBefore() {
        Device d = new DimmableSwitch();
        assertTrue(d.setId(1));
        assertEquals(1, d.getId());
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
    void shouldSetANewCopiedSwitchUsingConstructor() {
        Switch d = new Switch();
        d.setId(23);
        Device sd = new Switch(d);
        d.setId(23);

        assertEquals(23, sd.getId());
        assertTrue(d.isOn());
    }
    @Test
    void shouldSetANewCopiedSwitchUsingMakeCopy() {
        Switch d = new Switch();
        d.setId(23);
        Device sd = d.makeCopy();
        d.setId(23);

        assertEquals(sd.getIcon(), sd.getIcon());
        assertEquals(sd.isOn(), d.isOn());
        assertEquals(sd.getId(), d.getId());
    }

    @Test
    void shouldReturnCorrectLabelSwitch(){
        Switch s = new Switch();
        s.setOn(true);
        assertEquals(s.isOn(), s.getState());
        assertEquals(s.getLabel(), "on");

        s.click();
        assertEquals(s.isOn(), false);
    }

    @Test
    void shouldCreateCorrectStatelessDimmableSwitch(){
        StatelessDimmableSwitch s = new StatelessDimmableSwitch();
        assertNotNull(s);
        assertEquals(s.getLabel(), "-");
    }


    @Test
    void shouldCreateCopyStatelessDimmableSwitchUsingConstructor(){
        StatelessDimmableSwitch s = new StatelessDimmableSwitch();
        StatelessDimmableSwitch s2 = new StatelessDimmableSwitch(s);
        assertNotNull(s);
        assertEquals(s2.getLabel(), s.getLabel());
        assertEquals(s.isIncrementing(), s2.isIncrementing());

    }
    @Test
    void shouldCreateCopyStatelessDimmableSwitchMakeCopy(){
        StatelessDimmableSwitch s = new StatelessDimmableSwitch();
        StatelessDimmableSwitch s2 = s.makeCopy();
        assertNotNull(s);
        assertEquals(s2.getLabel(), s.getLabel());
        assertEquals(s.isIncrementing(), s2.isIncrementing());
    }

    @Test
    void shouldIncrementStatelessDimmableSwitch(){
        StatelessDimmableSwitch s = new StatelessDimmableSwitch();
        s.setIncrement(true);
        assertEquals(s.isIncrementing(), true);
        assertEquals(s.getLabel(), "+");
    }
}