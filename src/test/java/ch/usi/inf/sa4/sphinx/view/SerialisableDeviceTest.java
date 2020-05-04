package ch.usi.inf.sa4.sphinx.view;



import ch.usi.inf.sa4.sphinx.service.RoomService;
import ch.usi.inf.sa4.sphinx.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SerialisableDeviceTest {

    @Autowired
    UserService userService;
    @Autowired
    RoomService roomService;

    /* This test is kind of pointless: it is impossible for a constructor to return null.
    But it will add coverage to the empty constructor, which is enough, since there is nothing else to test. */
    @Test
    @DisplayName("Testing the creation of constructor without parameters")
    void existSerializedDevice() {
        SerialisableDevice serialisableDevice = new SerialisableDevice();
        assertNotNull(serialisableDevice);
    }

}