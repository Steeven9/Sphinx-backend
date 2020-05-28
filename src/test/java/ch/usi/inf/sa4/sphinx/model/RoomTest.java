package ch.usi.inf.sa4.sphinx.model;
import ch.usi.inf.sa4.sphinx.view.SerialisableDevice;

import ch.usi.inf.sa4.sphinx.view.SerialisableDevice;
import ch.usi.inf.sa4.sphinx.view.SerialisableRoom;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class RoomTest {

    @Test
    void TestConstructor() {
        SerialisableRoom serRoom = new SerialisableRoom();
        serRoom.setBackground(null);
        serRoom.setIcon(null);
        serRoom.setName(null);
        Room room = new Room(serRoom);
        assertEquals("Room", room.getName());
        assertEquals("./img/icons/rooms/icon-generic-room.svg", room.getIcon());
        assertEquals("./img/backgrounds/rooms/background-generic-room.svg", room.getBackground());
    }

    @Test
    void testUserNullAddRoom() {
        User user = new User();
        assertThrows(NullPointerException.class, () -> user.addRoom(null));
    }

    @Test
    void coverUserSerializeAsHost() {
        User user = new User();
        user.setUsername("blah");
        user.setEmail("blah");
        user.setFullname("blah");
        user.serialiseAsHost();
    }

    @Test
    void testAddDevice() {
        Room room = new Room();
        assertThrows(IllegalArgumentException.class, () -> room.addDevice(null));
    }

//    Room r = new Room();
//
//    @Test
//    void shouldCreateRoomLikeSerialisableRoom(){
//        SerialisableRoom sr = new SerialisableRoom();
//        sr.devices = new Integer[]{1,2,4};
//        Room room = new Room(sr);
//        assertEquals(sr.id, room.getId());
//        assertEquals(sr.name, room.getName());
//        assertEquals(sr.background, room.getBackground());
//        assertEquals(sr.icon, room.getIcon());
//    }
//
//    @Test
//    void shouldReturnTrueIfTheTwoCopiesAreEqual() {
//        Room newRoom = r.makeCopy();
//        assertEquals(newRoom.getDevices(),r.getDevices());
//        assertEquals(newRoom.getName(),r.getName());
//        assertEquals(newRoom.getId(),r.getId());
//        assertEquals(newRoom.getBackground(),r.getBackground());
//    }
//
//    @Test
//    void shouldSetIdToInteger() {
//        assertTrue(r.setId(1));
//    }
//
//    @Test
//    void shouldReturnNullWithIdEqualToNull() {
//        r.setId(null);
//        assertEquals(null, r.getId());
//    }
//
//    @Test
//    void shouldReturnIdNumber() {
//        r.setId(3);
//        assertEquals(3, r.getId());
//    }
//
//    @Test
//    void shouldReturnNameOnSettingNameToString() {
//        r.setName("TEST_NAME");
//        assertEquals("TEST_NAME", r.getName());
//    }
//
//    @Test
//    void shouldReturnNullOnSettingNameToNull() {
//        r.setName(null);
//        assertEquals(null, r.getName());
//    }
//
//    @Test
//    void shouldReturnBackgroundOnSettingNameToString() {
//        r.setBackground("TEST_BG");
//        assertEquals("TEST_BG", r.getBackground());
//    }
//
//    @Test
//    void shouldReturnNullOnSettingBackgroundToNull() {
//        r.setBackground(null);
//        assertEquals(null, r.getBackground());
//    }
//
//    @Test
//    void shouldReturnIconOnSettingIconToString() {
//        r.setIcon("TEST_ICON");
//        assertEquals("TEST_ICON", r.getIcon());
//    }
//
//    @Test
//    void shouldReturnNullOnSettingIconToNull() {
//        r.setIcon(null);
//        assertEquals(null, r.getIcon());
//    }
//
//    @Test
//    void shouldReturnListOnSettingDevicesToIntegerList() {
//
//        int test_device1 = 1;
//        r.addDevice(test_device1);
//
//        int test_device2 = 2;
//        r.addDevice(test_device2);
//
//        List<Integer> test_devices = Arrays.asList(test_device1, test_device2);
//        assertEquals(test_devices, r.getDevices());
//    }
//
//    @Test
//    void shouldReturnNullOnSettingDevicesToNull() {
//        List<Integer> test_devices = Arrays.asList(null, null);
//        r.addDevice(null);
//        r.addDevice(null);
//        assertEquals(test_devices, r.getDevices());
//
//    }
//
//    @Test
//    void shouldReturnDevicesWithTheDeviceRemoved() {
//        int test_device1 = 1;
//        int test_device2 = 2;
//        int test_device3 = 3;
//        int test_device4 = 4;
//        int test_device5 = 5;
//        int test_device6 = 6;
//        int test_device7 = 7;
//        List<Integer> test_devices = Arrays.asList(test_device1, test_device2, test_device3, test_device5, test_device6, test_device7);
//        r.addDevice(test_device1);
//        r.addDevice(test_device2);
//        r.addDevice(test_device3);
//        r.addDevice(test_device4);
//        r.addDevice(test_device5);
//        r.addDevice(test_device6);
//        r.addDevice(test_device7);
//        r.removeDevice(4);
//        assertEquals(test_devices, r.getDevices());
//    }
//
//    @Test
//    void shouldReturnSerialisebleVersion() {
//        Room d = new Room();
//        d.setName("TEST_NAME");
//        d.setId(23);
//        SerialisableRoom sd = d.serialise();
//        assertEquals("TEST_NAME", sd.name);
//        assertEquals(23, sd.id);
//    }

}
