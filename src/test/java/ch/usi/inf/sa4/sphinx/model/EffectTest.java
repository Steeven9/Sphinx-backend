package ch.usi.inf.sa4.sphinx.model;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class EffectTest {

    @Disabled
    @Test
    void shouldReturnTrueIfSettingIdIsSetBefore() {
        DimmableLightStateSet d = new DimmableLightStateSet(9);
        assertNull(d.getId());
    }

    @Disabled
    @Test
    void shouldReturnFalseIfSettingIdIsSetBefore() {
        Device d = new Light();
        d.setId(1);
        assertFalse(d.setId(4283));
        assertEquals(1, d.getId());
    }

    @Test
    void setId() {
    }

    @Test
    void getId() {
    }
}