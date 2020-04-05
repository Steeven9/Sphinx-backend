package ch.usi.inf.sa4.sphinx.model;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class LightTest {

    @Test
    void shouldReturnCopyIfMakeCopy() {
        Light l = new Light();
        l.setName("TEST_LIGHT");
<<<<<<< HEAD
=======
        l.couplings = new ArrayList<>();
>>>>>>> #61: added tests for Effect, LightSensor and Light, SmartPlug and Devices improved

        Device l2 = l.makeCopy();
        assertNotNull(l2);
        assertEquals(l.getId(), l2.getId());
        assertEquals(l.getName(), l2.getName());
<<<<<<< HEAD
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

=======
    }

    @Test
    void shouldReturnOnLabelIfCalledGetLabel() {
        Light l = new Light();
        l.setOn(true);
        l.couplings = new ArrayList<>();

        assertEquals("on",l.getLabel());
    }

    @Test
    void shouldReturnOffLabelIfCalledGetLabel() {
        Light l = new Light();
        l.setOn(false);
        l.couplings = new ArrayList<>();

        assertEquals("off",l.getLabel());
    }

>>>>>>> #61: added tests for Effect, LightSensor and Light, SmartPlug and Devices improved
}