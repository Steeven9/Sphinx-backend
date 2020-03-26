package ch.usi.inf.sa4.sphinx.Demo;


import ch.usi.inf.sa4.sphinx.model.Light;
import ch.usi.inf.sa4.sphinx.misc.DeviceType;
import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.Room;
import ch.usi.inf.sa4.sphinx.model.User;
import ch.usi.inf.sa4.sphinx.service.DeviceService;
import ch.usi.inf.sa4.sphinx.service.RoomService;
import ch.usi.inf.sa4.sphinx.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

public class DummyDataAdder {


    @Autowired
    private UserService userService;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private RoomService roomService;

    private void data1(){
        User newuUser = new User("rand@usi.ch", "1234", "farp", "rand farp");
        userService.insert(newuUser);
        Room newRoom1 = new Room();
        newRoom1.setName("room1");
        Room newRoom2 = new Room();
        newRoom2.setName("room2");
        Integer roomId1 = userService.addRoom("farp", newRoom1);
        Integer roomId2 = userService.addRoom("farp", newRoom2);
        Device newDevice = new Light();
        roomService.addDevice(roomId1,DeviceType.DIMMABLE_LIGHT );
        roomService.addDevice(roomId1,DeviceType.LIGHT_SENSOR);
        roomService.addDevice(roomId2,DeviceType.LIGHT );

    }

    public void dummy1(){
        data1();
    }

}
