package ch.usi.inf.sa4.sphinx.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeviceTest {

    @Test
    void shouldReturnTrueIfCronstructorCreatesNotNull() {
        Device d = new Dimmable();
        assertNotNull(d);
    }

    @Test
    void shouldReturnTrueIfSettingIdIsNotSetBefore() {
        Device c = new Dimmable();
        assertTrue(c.setId(1));
    }

    @Test
    void shouldReturnFalseIfSettingIdIsSetBefore() {
        Device c = new Light();
        c.setId(1);
        assertFalse(c.setId(4283));
    }

    @Test
    void should() {
    }

    @Test
    void setIcon() {
    }

    @Test
    void setName() {
    }

    @Test
    void getId() {
    }

    @Test
    void getIcon() {
    }

    @Test
    void getName() {
    }

    @Test
    void isOn() {
    }

    @Test
    void setOn() {
    }

    @Test
    void getLabel() {
    }

    @Test
    void addObserver() {
    }

    @Test
    void triggerEffects() {
    }
}