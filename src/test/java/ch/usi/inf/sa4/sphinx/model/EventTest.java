package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.service.DeviceService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EventTest {
    @Test
    void testingConstructorCreatesNotNullObjectForStatelessDimmSwitch(){
        StatelessDimmSwitchChangedEvent s = new StatelessDimmSwitchChangedEvent(4, 100.0);//mock-up object testing if is not null
        assertNotNull(s);
    }

    @Test
    void testingMakeCopyStateless() {
        StatelessDimmSwitchChangedEvent s = new StatelessDimmSwitchChangedEvent(4, 100.0);//mock-up object testing if is not null
        Event<Double> s2 = s.makeCopy();
        assertEquals(s.deviceId, s2.deviceId);
    }

    @Test
    void testingConstructorCreatesNotNullObjectForDimmSwitch(){
        DimmSwitchChangedEvent s = new DimmSwitchChangedEvent(4);//mock-up object testing if is not null
        assertNotNull(s);
    }

    @Test
    void testingMakeCopyForDimmSwitch(){
        DimmSwitchChangedEvent s = new DimmSwitchChangedEvent(4);//mock-up object testing if is not null
        DimmSwitchChangedEvent s2  = s.makeCopy();
        assertNotNull(s2);
        assertEquals(s.getDeviceId(), s2.getDeviceId());
    }

    @Disabled("Issue number #109. Throws a NullPointerException since deviceService is null inside get() method")
    @Test
    void testingGetReturnsCorrectDoubleDimmSwitch(){
        DimmSwitchChangedEvent s = new DimmSwitchChangedEvent(4);//mock-up object testing if is not null
        double d = s.get();
    }


}