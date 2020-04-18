package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CouplingServiceTest {

    @Autowired
    CouplingService couplingService;
    @Autowired
    VolatileEffectStorage effectStorage;

    @Test
    @DisplayName("Test adding a new coupling")
    void testSimpleScenario() {
        Event<Double> event = new DimmSwitchChangedEvent(1);
        Effect<Double> effect = new DimmableLightStateInc(2);

        assertAll("test with invalid values",
                () -> assertNull(couplingService.getEffect(1)),
                () -> assertNull(couplingService.getEvent(1)),
                () -> assertNull(couplingService.get(1)),
                () -> assertNull(couplingService.get(null)),
                () -> assertNull(couplingService.getEvent(null)),
                () -> assertNull(couplingService.getEffect(null)),
                () -> assertNull(couplingService.getEffects(null)),
                () -> assertNull(couplingService.getEffects(1))
        );

        Integer id = couplingService.addCoupling(event, effect);
        assertNotNull(couplingService.get(id));
        assertEquals(1, couplingService.getEvent(id).getDeviceId());

        couplingService.delete(id);
        assertNull(couplingService.get(id));
    }


    @Test
    @DisplayName("test adding effects")
    @Disabled(value = "fix addEffect method: should check that second parameter is not null")
    void test() {
        StatelessDimmSwitchChangedEvent event = new StatelessDimmSwitchChangedEvent(3, 0.6);
        DimmableLightStateSet effect = new DimmableLightStateSet(4);

        assertAll("test with invalid values",
                () -> assertThrows(NullPointerException.class, () -> couplingService.addCoupling(null, null)),
                () -> assertThrows(NullPointerException.class, () -> couplingService.addCoupling(event, null)),
                () -> assertThrows(NullPointerException.class, () -> couplingService.addCoupling(null, effect)),
                () -> assertFalse(couplingService.addEffect(999, null)),
                () -> assertFalse(couplingService.addEffect(null, null))
        );

        Integer id = couplingService.addCoupling(event, effect);
        assertNotNull(couplingService.get(id));

        assertEquals(1, couplingService.getEffects(id).size());

        DimmableLightStateSet effect1 = new DimmableLightStateSet(5);
        Integer effectId = effectStorage.insert(effect1);
        assertFalse(couplingService.addEffect(id, null));
        assertTrue(couplingService.addEffect(id, effectId));

        assertEquals(2, couplingService.getEffects(id).size());
    }
}