package ch.usi.inf.sa4.sphinx.model;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class EffectTest {
    @Disabled
    @Test
    void shouldReturnFalseIfSettingIdIsSetBefore() {
        Device d = new Light();
        d.setId(1);
        assertFalse(d.setId(4283));
    }

}