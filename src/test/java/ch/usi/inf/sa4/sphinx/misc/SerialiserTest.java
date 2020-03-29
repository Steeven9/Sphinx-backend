package ch.usi.inf.sa4.sphinx.misc;

import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.Light;
import ch.usi.inf.sa4.sphinx.model.Room;
import ch.usi.inf.sa4.sphinx.model.User;
import ch.usi.inf.sa4.sphinx.service.RoomService;
import ch.usi.inf.sa4.sphinx.service.UserService;
import ch.usi.inf.sa4.sphinx.view.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SerialiserTest {


    private final String username = "randomUsername";
    @Autowired
    UserService userService;


    Device device;
    User user;
    Room room;
    @Autowired
    RoomService roomService;

    @BeforeEach
    void createDeviceAndUser() {
        device = new Light();
        device.setId(1);
        room = new Room();
        room.setId(2);
        user = new User("randomEmail", "randomPassword", username, "randomFullname");
    }

    @Disabled(value = "fix Device.setId(Integer) method")
    @Test
    @DisplayName("Transform this device in a serialiseDevice")
    void isSerializedDevice() {

    }
}