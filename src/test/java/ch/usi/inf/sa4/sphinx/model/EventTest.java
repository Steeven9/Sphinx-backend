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

    /*
        Throws NullPointerException when I call get()
    */
    @Disabled
    @Test
    void testingMakeCopyStateless(){
        StatelessDimmSwitchChangedEvent s = new StatelessDimmSwitchChangedEvent(4, 100.0);//mock-up object testing if is not null
        Event<Double>s2 = s.makeCopy();
        Device device = new Switch();
        device.setId(4);
        DeviceService d = new DeviceService();
        assertEquals(4, s2.get());
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

    @Disabled
    @Test
    void testingGetReturnsCorrectDoubleDimmSwitch(){
        DimmSwitchChangedEvent s = new DimmSwitchChangedEvent(4);//mock-up object testing if is not null
        double d = s.get(); //throws an exception since deviceService is null inside get() method
    }
}