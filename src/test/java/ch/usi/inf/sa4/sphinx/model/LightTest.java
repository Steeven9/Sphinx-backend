package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.service.CouplingService;
import ch.usi.inf.sa4.sphinx.service.RoomService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LightTest {

    @Autowired
    RoomService roomService;
    @Autowired
    CouplingService couplingService;

    @Test
    void shouldReturnCopyIfMakeCopy() {
        Light l = new Light(roomService, couplingService);
        l.setName("TEST_LIGHT");

        Device l2 = l.makeCopy();
        assertNotNull(l2);
        assertEquals(l.getId(), l2.getId());
        assertEquals(l.getName(), l2.getName());
    }

    @Test
    void shouldReturnOnLabelIfCalledGetLabel() {
        Light l = new Light(roomService, couplingService);
        l.setOn(true);
        assertEquals("on",l.getLabel());
    }

    @Test
    void shouldReturnOffLabelIfCalledGetLabel() {
        Light l = new Light(roomService, couplingService);
        l.setOn(false);
        assertEquals("off",l.getLabel());
    }

}