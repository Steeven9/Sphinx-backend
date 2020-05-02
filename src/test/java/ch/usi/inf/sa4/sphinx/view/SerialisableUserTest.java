package ch.usi.inf.sa4.sphinx.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class SerialisableUserTest {

    /* This test is kind of pointless: it is impossible for a constructor to return null.
    But it will add coverage to the empty constructor, which is enough, since there is nothing else to test. */
    @Test
    @DisplayName("Testing creation of constructor without parameters")
    void existSerializedUser() {
        SerialisableUser serialisableUser = new SerialisableUser();
        assertNotNull(serialisableUser);
    }

}