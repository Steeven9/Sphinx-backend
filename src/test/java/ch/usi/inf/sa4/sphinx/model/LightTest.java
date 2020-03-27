package ch.usi.inf.sa4.sphinx.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LightTest {

    @Test
    void shouldReturnCopyIfMakeCopy() {
        Light l = new Light();
        l.setName("TEST_LIGHT");

        Device l2 = l.makeCopy();
        assertNotNull(l2);
        assertEquals(l.getId(), l2.getId());
        assertEquals(l.getName(), l2.getName());
    }

    @Test
    void shouldReturnOnLabelIfCalledGetLabel() {
        Light l = new Light();
        l.setOn(true);
        assertEquals("on",l.getLabel());
    }

    @Test
    void shouldReturnOffLabelIfCalledGetLabel() {
        Light l = new Light();
        l.setOn(false);
        assertEquals("off",l.getLabel());
    }

}