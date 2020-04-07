package ch.usi.inf.sa4.sphinx.model;
import ch.usi.inf.sa4.sphinx.view.SerialisableDevice;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DimmableLightTest {
    @Test
    void shouldReturnIntensityCorrectly() {
        DimmableLight light = new DimmableLight();
        assertNotNull(light);
        assertEquals(1.0, light.getIntensity());
    }
}