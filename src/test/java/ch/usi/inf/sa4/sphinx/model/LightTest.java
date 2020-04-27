package ch.usi.inf.sa4.sphinx.model;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class LightTest {


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