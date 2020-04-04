package ch.usi.inf.sa4.sphinx.misc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotImplementedExceptionTest {

    @Test
    void TestNotImplementedException() {
        assertNotNull(new NotImplementedException());
    }
}