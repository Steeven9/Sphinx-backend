package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.Demo.DummyDataAdder;
import ch.usi.inf.sa4.sphinx.misc.DeviceType;
import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.Light;
import ch.usi.inf.sa4.sphinx.model.Room;
import ch.usi.inf.sa4.sphinx.model.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DeviceServiceTest {

    @Autowired
    DeviceService deviceService;
    @Autowired
    RoomService roomService;
    @Autowired
    UserService userService;
    @Autowired
    RoomStorage roomStorage;

    private static final String username = "testUser";
    User user;



    @BeforeAll
    void beforeAll(){
        userService.delete(username);
        userService.delete("User2");
    }

    @BeforeEach
    void setUp() {
        User newUser = new User("test@mail.com", "1234", username, "mario rossi");
        userService.insert(newUser);
        user = userService.get(username).get();
        user.addRoom(new Room());
        userService.update(user);
        user = userService.get(user.getUsername()).get();
    }

    @AfterEach
    void clean() {
        userService.delete(username);
        userService.delete("User2");
    }

    @Test
    @DisplayName("Tests both get and update methods")
    void testDeviceService() {
        assertTrue(deviceService.get(9999).isEmpty());
        Integer roomId = user.getRooms().get(0).getId();


        Integer deviceId = roomService.addDevice(roomId, DeviceType.LIGHT).get();
        Device returnedDevice = deviceService.get(deviceId).get();
        assertEquals("Device", returnedDevice.getName());

        String deviceName = "secondTestName";
        returnedDevice.setName(deviceName);

        assertTrue(deviceService.update(returnedDevice));

        returnedDevice = deviceService.get(deviceId).get();
        assertEquals(deviceName, deviceService.get(deviceId).get().getName());


        Device newDevice =new Light();
        assertFalse(deviceService.update(new Light()));
    }








}