package ch.usi.inf.sa4.sphinx.model;
import ch.usi.inf.sa4.sphinx.misc.DeviceFactory;
import ch.usi.inf.sa4.sphinx.model.Coupling.Coupling;
import ch.usi.inf.sa4.sphinx.model.Coupling.SwitchToDevice;
import ch.usi.inf.sa4.sphinx.model.conditions.*;
import ch.usi.inf.sa4.sphinx.model.triggers.*;
import ch.usi.inf.sa4.sphinx.view.SerialisableDevice;
import org.junit.jupiter.api.Disabled;
import ch.usi.inf.sa4.sphinx.view.SerialisableDevice;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeviceTest {

    //TODO isnt dimmable supposed to be abstract?
//    @Test
//    void shouldReturnTrueIfConstructorCreatesNotNull() {
//        Device d = new Dimmable();
//        assertNotNull(d);
//    }
//
//
//
//
    @Test
    void coverDeviceFactory() {
        DeviceFactory df = new DeviceFactory();
    }

    @Test
    void coverMotionSensor() {
        MotionSensor ms = new MotionSensor();
        SerialisableDevice sd = new SerialisableDevice();
        ms.setPropertiesFrom(sd);
        sd.setQuantity(10.0);
        ms.setPropertiesFrom(sd);
        sd.setQuantity(null);
        ms.setPropertiesFrom(sd);
    }

    @Test
    void shouldSetIcon() {
        Device d = new Switch();
        d.setIcon("TEST_ICON");
        assertEquals("TEST_ICON", d.getIcon());
    }
//
    @Test
    void shouldSetName() {
        Device d = new MotionSensor();
        d.setName("TEST_NAME");
        assertEquals("TEST_NAME", d.getName());
    }
//
    @Test
    void shouldReturnTrueIfIsOn() {
        Device d = new DimmableLight();
        d.setOn(true);
        assertTrue(d.isOn());
    }
//
    @Test
    void shouldReturnFalseIfIsOff() {
        Device d = new HumiditySensor();
        d.setOn(false);
        assertFalse(d.isOn());
    }

    @Test
    void shouldAddObserver() {
        Switch d1 = new Switch();
        Device d2 = new DimmableLight();
        Coupling observer = new SwitchToDevice(d1, d2);
        d1.addObserver(observer);
        List<Observer> observers = d1.getObservers();
        assertTrue(observers.contains(observer));
    }

    @Test
    void testUserAddNullRoom() {
        User user = new User();
        assertThrows(NullPointerException.class, () -> user.addRoom(null));
    }

    @Test
    void testStatelessDimmableSwitchSetState() {
        StatelessDimmableSwitch d = new StatelessDimmableSwitch();
        assertFalse(d.isIncrementing());
        assertEquals("-", d.getLabel());
        d.setIncrement(true);
        assertTrue(d.isIncrementing());
        assertEquals("+", d.getLabel());
        SerialisableDevice sd = new SerialisableDevice();
        d.setPropertiesFrom(sd);
    }

    @Test
    void coverConditionTargetType() {
        ConditionTargetType x = ConditionTargetType.EQUAL_TARGET;
        x = ConditionTargetType.GREATER_TARGET;
        x = ConditionTargetType.SMALLER_TARGET;
        x = ConditionTargetType.UNEQUAL_TARGET;
    }

    @Test
    void testMotionCondition() {
        MotionCondition mc = new MotionCondition(new MotionSensor(), true, MotionCondition.Operator.EQUAL);
        MotionCondition mc2 = new MotionCondition(new MotionSensor(), false, MotionCondition.Operator.NOT_EQUAL);
        assertFalse(mc.check());
        assertFalse(mc2.check());
        assertEquals(ConditionType.MOTION_DETECTED, mc.getConditionType());
        assertEquals(ConditionType.MOTION_NOT_DETECTED, mc2.getConditionType());
    }

    @Test
    void testConditionFactory() {
        ConditionFactory cf = new ConditionFactory();
        cf.make(new Light(), new Object(), ConditionType.DEVICE_ON);
        cf.make(new Light(), new Object(), ConditionType.DEVICE_OFF);
        assertThrows(NullPointerException.class, () -> cf.make(new Light(), new Object(), null));
        assertThrows(NullPointerException.class, () -> cf.make(new Light(), null, null));
        assertThrows(NullPointerException.class, () -> cf.make(null, null, null));
        assertThrows(IllegalArgumentException.class, () -> cf.make(new Light(), new Object(), ConditionType.MOTION_DETECTED));
    }

    @Test
    void testOnCondition() {
        OnCondition oc = new OnCondition(new Light(), OnCondition.Operator.ON);
        OnCondition oc2 = new OnCondition(new Light(), OnCondition.Operator.OFF);
        assertTrue(oc.check());
        assertFalse(oc2.check());
        assertEquals(ConditionType.DEVICE_ON, oc.getConditionType());
        assertEquals(ConditionType.DEVICE_OFF, oc2.getConditionType());
    }

    @Test
    void testSensorQuantityCondition() {
        SensorQuantityCondition sqc = new SensorQuantityCondition(new TempSensor(), 2.0, SensorQuantityCondition.Operator.GREATER);
        SensorQuantityCondition sqc2 = new SensorQuantityCondition(new TempSensor(), 2.0, SensorQuantityCondition.Operator.SMALLER);
        SensorQuantityCondition sqc3 = new SensorQuantityCondition(new TempSensor(), 2.0, SensorQuantityCondition.Operator.EQUAL);
        assertTrue(sqc.check());
        assertFalse(sqc2.check());
        assertFalse(sqc2.check());
        assertEquals(ConditionType.SENSOR_OVER, sqc.getConditionType());
        assertEquals(ConditionType.SENSOR_UNDER, sqc2.getConditionType());
        assertEquals(ConditionType.SENSOR_UNDER, sqc3.getConditionType());
    }

    @Test
    void testConditionType() {
        assertEquals(ConditionType.DEVICE_ON, ConditionType.intToType(1));
        assertEquals(ConditionType.DEVICE_OFF, ConditionType.intToType(2));
        assertEquals(ConditionType.MOTION_DETECTED, ConditionType.intToType(3));
        assertEquals(ConditionType.MOTION_NOT_DETECTED, ConditionType.intToType(4));
        assertEquals(ConditionType.SENSOR_OVER, ConditionType.intToType(5));
        assertEquals(ConditionType.SENSOR_UNDER, ConditionType.intToType(6));
        assertThrows(IllegalArgumentException.class, () -> ConditionType.intToType(0));
        assertEquals(1, ConditionType.DEVICE_ON.toInt());
        assertEquals(2, ConditionType.DEVICE_OFF.toInt());
        assertEquals(3, ConditionType.MOTION_DETECTED.toInt());
        assertEquals(4, ConditionType.MOTION_NOT_DETECTED.toInt());
        assertEquals(5, ConditionType.SENSOR_OVER.toInt());
        assertEquals(6, ConditionType.SENSOR_UNDER.toInt());
    }

    @Test
    void coverChanged() {
        MotionChanged mc = new MotionChanged(new MotionSensor(), new Automation(new User()), true, MotionCondition.Operator.EQUAL);
        OnChanged oc = new OnChanged(new Light(), new Automation(new User()), OnCondition.Operator.ON);
        SensorChanged sc = new SensorChanged(new TempSensor(), new Automation(new User()), 2.0, SensorQuantityCondition.Operator.GREATER);
    }

    @Test
    void testTriggerFactory() {
        TriggerFactory tf = new TriggerFactory();
        tf.makeEvent(new Light(), new Object(), ConditionType.DEVICE_ON, new Automation(new User()));
        tf.makeEvent(new Light(), new Object(), ConditionType.DEVICE_OFF, new Automation(new User()));
        assertThrows(NullPointerException.class, () -> tf.makeEvent(new Light(), new Object(), ConditionType.DEVICE_ON, null));
        assertThrows(NullPointerException.class, () -> tf.makeEvent(new Light(), new Object(), null, null));
        assertThrows(NullPointerException.class, () -> tf.makeEvent(new Light(), null, null, null));
        assertThrows(NullPointerException.class, () -> tf.makeEvent(null, null, null, null));
        assertThrows(IllegalArgumentException.class, () -> tf.makeEvent(new Light(), new Object(), ConditionType.MOTION_DETECTED, new Automation(new User())));
    }
//
//    @Test
//    void shouldSetANewCopiedSwitchUsingConstructor() {
//        Switch d = new Switch();
//        d.setId(23);
//        Device sd = new Switch(d);
//        d.setId(23);
//
//        assertEquals(23, sd.getId());
//        assertTrue(d.isOn());
//    }
//    @Test
//    void shouldSetANewCopiedSwitchUsingMakeCopy() {
//        Switch d = new Switch();
//        d.setId(23);
//        Device sd = d.makeCopy();
//        d.setId(23);
//
//        assertEquals(sd.getIcon(), sd.getIcon());
//        assertEquals(sd.isOn(), d.isOn());
//        assertEquals(sd.getId(), d.getId());
//    }
//
//    @Test
//    void shouldReturnCorrectLabelSwitch(){
//        Switch s = new Switch();
//        s.setOn(true);
//        assertEquals(s.isOn(), s.getState());
//        assertEquals(s.getLabel(), "on");
//
//        s.click();
//        assertEquals(s.isOn(), false);
//    }
//
//    @Test
//    void shouldCreateCorrectStatelessDimmableSwitch(){
//        StatelessDimmableSwitch s = new StatelessDimmableSwitch();
//        assertNotNull(s);
//        assertEquals(s.getLabel(), "-");
//    }
//
//
//    @Test
//    void shouldCreateCopyStatelessDimmableSwitchUsingConstructor(){
//        StatelessDimmableSwitch s = new StatelessDimmableSwitch();
//        StatelessDimmableSwitch s2 = new StatelessDimmableSwitch(s);
//        assertNotNull(s);
//        assertEquals(s2.getLabel(), s.getLabel());
//        assertEquals(s.isIncrementing(), s2.isIncrementing());
//
//    }
//    @Test
//    void shouldCreateCopyStatelessDimmableSwitchMakeCopy(){
//        StatelessDimmableSwitch s = new StatelessDimmableSwitch();
//        StatelessDimmableSwitch s2 = s.makeCopy();
//        assertNotNull(s);
//        assertEquals(s2.getLabel(), s.getLabel());
//        assertEquals(s.isIncrementing(), s2.isIncrementing());
//    }
//
//    @Test
//    void shouldIncrementStatelessDimmableSwitch(){
//        StatelessDimmableSwitch s = new StatelessDimmableSwitch();
//        s.setIncrement(true);
//        assertEquals(s.isIncrementing(), true);
//        assertEquals(s.getLabel(), "+");
//    }
}