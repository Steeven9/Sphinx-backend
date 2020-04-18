package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CouplingServiceTest {

    @Autowired
    CouplingService couplingService;
    @Autowired
    VolatileEffectStorage effectStorage;
    @Autowired
    VolatileEventStorage eventStorage;

    @Test
    @DisplayName("Test adding a new coupling")
    void testSimpleScenario() {
        Event<Double> event = new DimmSwitchChangedEvent(1);
        Effect<Double> effect = new DimmableLightStateInc(2);

        assertAll("test with invalid values",
                () -> assertNull(couplingService.getEffect(1111)),
                () -> assertNull(couplingService.getEvent(1111)),
                () -> assertNull(couplingService.get(1111)),
                () -> assertNull(couplingService.get(null)),
                () -> assertNull(couplingService.getEvent(null)),
                () -> assertNull(couplingService.getEffect(null)),
                () -> assertNull(couplingService.getEffects(null)),
                () -> assertNull(couplingService.getEffects(1111))
        );

        Integer id = couplingService.addCoupling(event, effect);
        assertNotNull(couplingService.get(id));

        couplingService.delete(id);
        assertNull(couplingService.get(id));
    }

    @Test
    @DisplayName("test getting effect and event")
    void testGet() {
        StatelessDimmSwitchChangedEvent event = new StatelessDimmSwitchChangedEvent(30, 0.6);
        DimmableLightStateSet effect = new DimmableLightStateSet(90);

        Integer eventId = eventStorage.insert(event);
        Integer effectId = effectStorage.insert(effect);
        assertEquals(event.getDeviceId(), couplingService.getEvent(eventId).getDeviceId());
//        assertEquals(effect.getEffectId(), couplingService.getEffect(effectId).getDeviceId()); future check
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

        List<Integer> list = couplingService.get(id).getEffectIds();
        assertEquals(1, list.size());
        assertEquals(1, couplingService.getEffects(id).size());

        Integer serachId = (Integer) couplingService.getEffects(id).get(0).getKey();
        assertTrue(list.contains(serachId));

        DimmableLightStateSet effect1 = new DimmableLightStateSet(5);
        Integer effectId = effectStorage.insert(effect1);
        assertFalse(couplingService.addEffect(id, null));
        assertTrue(couplingService.addEffect(id, effectId));

        list = couplingService.get(id).getEffectIds();
        assertEquals(2, couplingService.getEffects(id).size());
        assertEquals(2, list.size());

        Integer serachId1 = (Integer) couplingService.getEffects(id).get(1).getKey();
        assertTrue(list.contains(serachId1));
        assertTrue(list.contains(serachId));
    }
}