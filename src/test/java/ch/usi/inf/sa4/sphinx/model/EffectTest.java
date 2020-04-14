package ch.usi.inf.sa4.sphinx.model;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
class EffectTest {
    @Test
    void shouldReturnTrueIfSettingIdIsSetBefore() {
        DimmableLightStateSet d = new DimmableLightStateSet(9);
        assertNull(d.getKey());
    }
}