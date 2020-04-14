package ch.usi.inf.sa4.sphinx.model;
import ch.usi.inf.sa4.sphinx.view.SerialisableDevice;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class DimmableTest {

    @Test
    void shouldReturnSerialisebleVersion() {
        Dimmable d = new Dimmable();
        d.setOn(false);
        d.setName("TEST_NAME");
        d.setId(23);
        SerialisableDevice sd = d.serialise();
        assertEquals("TEST_NAME", sd.name);
        assertEquals(23, sd.id);
        assertFalse(d.isOn());
    }

    @Disabled
    @Test
    void shouldReturnTheStateAfterSettingItMultipleElements() {
        Dimmable d = new Dimmable();
        d.couplings.add(11);
    }
        @Test
        void shouldReturnTheStateAfterSettingIt () {
            Dimmable d = new Dimmable();
            d.setState(0.4);
            assertEquals(0.4, d.getState());
        }

        @Test
        void shouldThrowExceptionIfTheStateIsSetWrong () {
            Dimmable d = new Dimmable();
            Dimmable d2 = new Dimmable(d);
            assertThrows(IllegalArgumentException.class, () -> d2.setState(1.1));
        }

        @Test
        void shouldReturnLabelCorrectly () {
            Dimmable d = new Dimmable();
            assertEquals("100.0%", d.getLabel());
        }

        @Test
        void shouldCopyDimmableCorrectly () {
            Dimmable d = new Dimmable();
            Dimmable d2 = d.makeCopy();
            assertEquals(d.couplings, d2.couplings);
        }

}