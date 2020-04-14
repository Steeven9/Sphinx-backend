package ch.usi.inf.sa4.sphinx.model;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
class EffectTest {
    @Test
    void shouldReturnTrueIfSettingIdIsSetBefore() {
        DimmableLightStateSet d = new DimmableLightStateSet(9);
        assertNull(d.getId());
    }

    @Disabled
    @Test
    void shouldReturnFalseIfSettingIdIsSetBefore() {
        Device d = new Light();
        d.setId(4283)
        assertEquals(4283, d.getId());
    }
}