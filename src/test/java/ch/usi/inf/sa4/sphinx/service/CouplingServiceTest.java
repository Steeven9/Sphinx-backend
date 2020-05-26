package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.Demo.DummyDataAdder;
import ch.usi.inf.sa4.sphinx.misc.DeviceType;
import ch.usi.inf.sa4.sphinx.model.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CouplingServiceTest {
    @Autowired
    RoomService roomService;
    @Autowired
    UserService userService;
    @Autowired
    RoomStorage roomStorage;
    @Autowired
    DummyDataAdder dummyDataAdder;
    @Autowired
    private CouplingService couplingService;
    private User user;
    private final static String username = "testUser";
    private final static Map<DeviceType, Integer> deviceIds = new HashMap<>();
    private static Integer roomId;
    @Autowired
    private DeviceService deviceService;

    @BeforeAll
    @DirtiesContext
    void wipe() {
    }

    @BeforeEach
    void setUp() {
        User newUser = new User("test@mail.com", "1234", username, "mario rossi");
        userService.insert(newUser);
        user = userService.get(username).get();
        roomId = userService.addRoom(username, new Room()).get();
        deviceIds.put(DeviceType.DIMMABLE_LIGHT, roomService.addDevice(roomId, DeviceType.DIMMABLE_LIGHT).get());
        deviceIds.put(DeviceType.STATELESS_DIMMABLE_SWITCH, roomService.addDevice(roomId, DeviceType.STATELESS_DIMMABLE_SWITCH).get());
        deviceIds.put(DeviceType.DIMMABLE_SWITCH, roomService.addDevice(roomId, DeviceType.DIMMABLE_SWITCH).get());
        deviceIds.put(DeviceType.SWITCH, roomService.addDevice(roomId, DeviceType.SWITCH).get());
        deviceIds.put(DeviceType.LIGHT, roomService.addDevice(roomId, DeviceType.LIGHT).get());
        deviceIds.put(DeviceType.TEMP_SENSOR, roomService.addDevice(roomId, DeviceType.TEMP_SENSOR).get());

    }

    @AfterEach
    void clean() {
        userService.delete(username);
    }


    @Test
    @DisplayName("Test adding a new coupling outside a transactional context")
    void testSwitchToDeviceCouplingNoTrans() {
        Device switched = deviceService.get(deviceIds.get(DeviceType.LIGHT)).get();
        Device switcher = deviceService.get(deviceIds.get(DeviceType.SWITCH)).get();
        assertTrue(switched.isOn());
        assertTrue(switcher.isOn());
        couplingService.createCoupling(switcher, switched);
        switched = deviceService.get(deviceIds.get(DeviceType.LIGHT)).get();
        switcher = deviceService.get(deviceIds.get(DeviceType.SWITCH)).get();
        switcher.setOn(false);
        deviceService.update(switcher);
        switched = deviceService.get(deviceIds.get(DeviceType.LIGHT)).get();
        switcher = deviceService.get(deviceIds.get(DeviceType.SWITCH)).get();
        assertFalse(switched.isOn());
        assertFalse(switcher.isOn());
        switcher.setOn(true);
        deviceService.update(switcher);
        switched = deviceService.get(deviceIds.get(DeviceType.LIGHT)).get();
        switcher = deviceService.get(deviceIds.get(DeviceType.SWITCH)).get();
        assertTrue(switched.isOn());
        assertTrue(switcher.isOn());
        couplingService.removeByDevicesIds(switcher.getId(), switched.getId());
        couplingService.removeByDevicesIds(switched.getId(), switcher.getId());
        switched = deviceService.get(deviceIds.get(DeviceType.LIGHT)).get();
        switcher = deviceService.get(deviceIds.get(DeviceType.SWITCH)).get();
        switcher.setOn(false);
        assertTrue(switched.isOn());
    }


    @Test
    @DisplayName("Test adding a new coupling within  a transactional context")
    @Transactional
    void testSwitchToDeviceCoupling() {
        Device switched = deviceService.get(deviceIds.get(DeviceType.LIGHT)).get();
        Device switcher = deviceService.get(deviceIds.get(DeviceType.SWITCH)).get();
        assertTrue(switched.isOn());
        assertTrue(switcher.isOn());
        couplingService.createCoupling(switcher, switched);
        switcher.setOn(false);
        assertAll(
                () -> assertFalse(switched.isOn()),
                () -> assertFalse(switcher.isOn())
        );
        switcher.setOn(true);
        assertAll(
                () -> assertTrue(switched.isOn()),
                () -> assertTrue(switcher.isOn())
        );
        couplingService.removeByDevicesIds(switcher.getId(), switched.getId());
        switcher.setOn(false);
        deviceService.update(switcher);
        assertAll(
                () -> assertTrue(switched.isOn()),
                () -> assertFalse(switcher.isOn())
        );



    }


    @Test
    @DisplayName("Test adding a new DimmSwitchToDimmLightCoupling")
    @Transactional
    void testDimmSwitchToDimmLightCoupling() {
        DimmableLight switched = (DimmableLight) deviceService.get(deviceIds.get(DeviceType.DIMMABLE_LIGHT)).get();
        DimmableSwitch switcher = (DimmableSwitch) deviceService.get(deviceIds.get(DeviceType.DIMMABLE_SWITCH)).get();
        switched.setState(0.1);
        assertEquals(switched.getIntensity(), 0.1);
        couplingService.createCoupling(switcher, switched);
        switcher.setState(0.5);
        assertEquals(switched.getIntensity(), 0.5);
    }

    @Test
    @DisplayName("Test adding a new StatelessDimmSwitchToDimmLightCoupling")
    @Transactional
    void testStatelessDimmSwitchToDimmLightCoupling() {
        DimmableLight switched = (DimmableLight) deviceService.get(deviceIds.get(DeviceType.DIMMABLE_LIGHT)).get();
        StatelessDimmableSwitch switcher = (StatelessDimmableSwitch) deviceService.get(deviceIds.get(DeviceType.STATELESS_DIMMABLE_SWITCH)).get();
        switched.setState(0.1);
        assertEquals(switched.getIntensity(), 0.1);
        couplingService.createCoupling(switcher, switched);
        switcher.setIncrement(true);
        assertEquals(switched.getIntensity(), 0.11);
    }


}

