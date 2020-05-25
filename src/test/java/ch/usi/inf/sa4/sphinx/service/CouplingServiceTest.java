package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.Demo.DummyDataAdder;
import ch.usi.inf.sa4.sphinx.misc.DeviceType;
import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.Room;
import ch.usi.inf.sa4.sphinx.model.User;
import ch.usi.inf.sa4.sphinx.service.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashMap;
import java.util.Map;



import ch.usi.inf.sa4.sphinx.model.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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
    private  static Integer roomId;
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
    @DisplayName("Test adding a new coupling")
    void testSwitchToDeviceCoupling(){
        Device switched = deviceService.get(deviceIds.get(DeviceType.LIGHT)).get();
        Device switcher = deviceService.get(deviceIds.get(DeviceType.SWITCH)).get();
        switched.setOn(true);
        switcher.setOn(true);
        assertAll(("on before switching"),
                ()->assertTrue(switched.isOn()),
                ()->assertTrue(switcher.isOn())
        );
        couplingService.createCoupling(switcher, switched);
        switcher.setOn(false);
        assertAll(("on after switching"),
                ()->assertFalse(switched.isOn()),
                ()->assertFalse(switcher.isOn())
        );
        switcher.setOn(true);
        assertAll(("on after switching"),
                ()->assertTrue(switched.isOn()),
                ()->assertTrue(switcher.isOn())
        );
        couplingService.removeByDevicesIds(switcher.getId(), switched.getId());
        couplingService.removeByDevicesIds(switched.getId(), switcher.getId());
        Device switcher2 = deviceService.get(deviceIds.get(DeviceType.SWITCH)).get();//lost synch, we must refetch from storage
        switcher2.setOn(false);
        assertTrue(switched.isOn());
    }

    @Test
    @DisplayName("Test adding a new coupling")
    @Disabled
    void testDimmSwitchToDimmLightCoupling(){
        DimmableLight switched = (DimmableLight) deviceService.get(deviceIds.get(DeviceType.DIMMABLE_LIGHT)).get();
        DimmableSwitch switcher =  (DimmableSwitch) deviceService.get(deviceIds.get(DeviceType.DIMMABLE_SWITCH)).get();
        switched.setState(0.1);
        assertEquals(switched.getIntensity(), 0.1);
        couplingService.createCoupling(switcher, switched);
        switcher.setState(0.5);
        assertEquals(switched.getIntensity(), 0.5);
    }

    @Test
    @DisplayName("Test adding a new coupling")
    @Disabled
    void testStatelessDimmSwitchToDimmLightCoupling(){
        DimmableLight switched = (DimmableLight) deviceService.get(deviceIds.get(DeviceType.DIMMABLE_LIGHT)).get();
        StatelessDimmableSwitch switcher =  (StatelessDimmableSwitch) deviceService.get(deviceIds.get(DeviceType.STATELESS_DIMMABLE_SWITCH)).get();
        switched.setState(0.1);
        assertEquals(switched.getIntensity(), 0.1);
        couplingService.createCoupling(switcher, switched);
        switcher.setIncrement(true);
        assertEquals(switched.getIntensity(), 0.2);
    }






}
//@SpringBootTest
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//class CouplingServiceTest {
//
//    @Autowired
//    CouplingService couplingService;
//    @Autowired
//    DummyDataAdder dummyDataAdder;
//    @Autowired
//    UserService userService;
//    @Autowired
//    RoomService roomService;
//
//    @BeforeAll
//    void init() {
//        dummyDataAdder.addDummyData();
//    }
//
//    @Test
//    @DisplayName("Test adding a new coupling")
//    @Disabled(value = "fix couplings")
//    void testSimpleScenario() {
//        Integer id1 = userService.getPopulatedRooms("user1").get(0).getId();
//        Integer switchId = roomService.addDevice(id1, DeviceType.SWITCH).get();
//        Integer lightId = userService.getPopulatedRooms("user1").get(0).getDevices().get(0).getId(); // light
//
//        Event<Double> event = new DimmSwitchChangedEvent(switchId);
//        Effect<Double> effect = new DimmableLightStateInc(lightId);
//
//        /*assertAll("test with invalid values",
//                () -> assertNull(couplingService.getEffect(1111)),
//                () -> assertNull(couplingService.getEvent(1111)),
//                () -> assertNull(couplingService.get(1111)),
//                () -> assertNull(couplingService.get(null)),
//                () -> assertNull(couplingService.getEvent(null)),
//                () -> assertNull(couplingService.getEffect(null)),
//                () -> assertNull(couplingService.getEffects(null)),
//                () -> assertNull(couplingService.getEffects(1111))
//        );*/
//
//        Integer id = couplingService.addCoupling(event, effect);
//        assertNotNull(couplingService.get(id));
//
//        couplingService.delete(id);
//        assertNull(couplingService.get(id));
//    }

   /* @Test
    @DisplayName("test getting effect and event")
    void testGet() {
        StatelessDimmSwitchChangedEvent event = new StatelessDimmSwitchChangedEvent(30, 0.6);
        DimmableLightStateSet effect = new DimmableLightStateSet(90);

        Integer eventId = eventStorage.insert(event);
        Integer effectId = effectStorage.insert(effect);
        assertEquals(event.getDeviceId(), couplingService.getEvent(eventId).getDeviceId());
        assertEquals(effect.getEffectId(), couplingService.getEffect(effectId).getDeviceId()); future check
    }*/


    /*@Test
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
    }*/
//}
