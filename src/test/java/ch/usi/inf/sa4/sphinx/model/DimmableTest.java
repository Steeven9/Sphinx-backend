package ch.usi.inf.sa4.sphinx.model;
import ch.usi.inf.sa4.sphinx.service.CouplingService;
import ch.usi.inf.sa4.sphinx.service.RoomService;
import ch.usi.inf.sa4.sphinx.view.SerialisableDevice;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DimmableTest {

    @Autowired
    RoomService roomService;
    @Autowired
    CouplingService couplingService;

    @Test
    void shouldReturnSerialisebleVersion() {
        Dimmable d = new Dimmable(roomService, couplingService);
        d.setOn(false);
        d.setName("TEST_NAME");
        d.setId(23);
        SerialisableDevice sd = d.serialise();
        assertEquals("TEST_NAME", sd.name);
        assertEquals(23, sd.id);
        assertFalse(d.isOn());
    }

    @Test
    void shouldReturnTheStateAfterSettingItMultipleElements() {
        Dimmable d = new Dimmable(roomService, couplingService);
        d.couplings.add(11);
    }

    @Test
    void shouldReturnTheStateAfterSettingIt () {
        Dimmable d = new Dimmable(roomService, couplingService);
        d.setState(0.4);
        assertEquals(0.4, d.getState());
    }

    @Test
    void shouldThrowExceptionIfTheStateIsSetWrong () {
        Dimmable d = new Dimmable(roomService, couplingService);
        Dimmable d2 = new Dimmable(d);
        assertThrows(IllegalArgumentException.class, () -> d2.setState(1.1));
    }

    @Test
    void shouldReturnLabelCorrectly () {
        Dimmable d = new Dimmable(roomService, couplingService);
        assertEquals("100.0%", d.getLabel());
    }

    @Test
    void shouldCopyDimmableCorrectly () {
        Dimmable d = new Dimmable(roomService, couplingService);
        Dimmable d2 = d.makeCopy();
        assertEquals(d.couplings, d2.couplings);
    }

}