package ch.usi.inf.sa4.sphinx.misc;

import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.Light;
import ch.usi.inf.sa4.sphinx.model.Room;
import ch.usi.inf.sa4.sphinx.model.User;
import ch.usi.inf.sa4.sphinx.view.SerialisableDevice;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SerialiserTest {

    @Autowired
    private static Serialiser serialiser;


    SerialisableDevice serialisableDevice;
    Device device;
    User user;
    Room room;

    @Test
    void isSerializedDevice() {
    }
}