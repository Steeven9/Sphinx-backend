package ch.usi.inf.sa4.sphinx.model;
import ch.usi.inf.sa4.sphinx.service.CouplingService;
import ch.usi.inf.sa4.sphinx.service.RoomService;
import ch.usi.inf.sa4.sphinx.view.SerialisableDevice;

import ch.usi.inf.sa4.sphinx.view.SerialisableDevice;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DimmableLightTest {

    @Test
    void shouldReturnIntensityCorrectly() {
        DimmableLight light = new DimmableLight();
        assertNotNull(light);
        assertEquals(1.0, light.getIntensity());
    }
}