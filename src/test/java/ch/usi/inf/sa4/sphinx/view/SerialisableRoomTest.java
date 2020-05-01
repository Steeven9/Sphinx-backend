package ch.usi.inf.sa4.sphinx.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SerialisableRoomTest {

    /* This test is kind of pointless: it is impossible for a constructor to return null.
    But it will add coverage to the empty constructor, which is enough, since there is nothing else to test. */
    @Test
    @DisplayName("Testing the creation of constructor without parameters")
    void existSerializedRoom() {
        SerialisableRoom room = new SerialisableRoom();
        assertNotNull(room);
    }

}