package ch.usi.inf.sa4.sphinx.model;

import org.junit.jupiter.api.Test;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class CouplingTest {
    @Test
    void shouldReturnTheCorrectIntegerIfSettingId() {
        Coupling c = new Coupling(10, 11);
        c.setId(4283);
        assertEquals(4283, c.getId());
    }

    @Test
    void shouldReturnIntegerIfCallingGetterEventIdAndSetInConstructor () {
        Coupling c = new Coupling(10, 11);
        assertEquals(10, c.getEventId());
    }

    @Test
    void shouldReturnListOfIntegersIfCallingGetterEffectsSetInConstructor () {
        Coupling c = new Coupling(10, new ArrayList<Integer>(Arrays.asList(11, 19)));
        assertEquals(new ArrayList<Integer>(Arrays.asList(11, 19)), c.getEffectIds());
    }

    @Test
    void shouldReturnListOfIntegersIfCallingGetterEffectsSetInConstructorAndAddedEffect () {
        Coupling c = new Coupling(10, new ArrayList<Integer>(Arrays.asList(11, 19)));
        Integer e = 20;
        c.addEffect(e);
        assertEquals(new ArrayList<Integer>(Arrays.asList(11, 19, 20)), c.getEffectIds());
    }

    @Test
    void shouldReturnTrueIfCopyIsEqualToOriginalUsingMakeCopy () {
        Coupling c = new Coupling(10, new ArrayList<Integer>(Arrays.asList(11, 19)));
        Coupling newC = c.makeCopy();
    }
    Coupling c = new Coupling(10, 11);
}