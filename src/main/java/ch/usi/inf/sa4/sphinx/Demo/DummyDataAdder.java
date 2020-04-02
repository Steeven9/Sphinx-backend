package ch.usi.inf.sa4.sphinx.Demo;


import ch.usi.inf.sa4.sphinx.misc.DeviceType;
import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.Light;
import ch.usi.inf.sa4.sphinx.model.Room;
import ch.usi.inf.sa4.sphinx.model.User;
import ch.usi.inf.sa4.sphinx.service.DeviceService;
import ch.usi.inf.sa4.sphinx.service.RoomService;
import ch.usi.inf.sa4.sphinx.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.UUID;

@Component
public class DummyDataAdder {


    @Autowired
    private UserService userService;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private RoomService roomService;



    private void user1() {
        User newuUser = new User("mario@usi.ch", "1234", "mario", "mario rossi");
        Boolean inserted = userService.insert(newuUser);
        Room newRoom1 = new Room();
        newRoom1.setName("room1");
        Room newRoom2 = new Room();
        newRoom2.setName("room2");
        Integer roomId1 = userService.addRoom("mario", newRoom1);
        Integer roomId2 = userService.addRoom("mario", newRoom2);
//        Device newDevice = new Light();
//        roomService.addDevice(roomId1, DeviceType.DIMMABLE_LIGHT);
//        roomService.addDevice(roomId1, DeviceType.LIGHT_SENSOR);
//        roomService.addDevice(roomId2, DeviceType.LIGHT);

    }


    private void user2() {
        User newuUser = new User("mario2@usi.ch", "1234", "mario2", "mario rossi");
        userService.insert(newuUser);
        Room newRoom1 = new Room();
        newRoom1.setName("Living Room");
        Room newRoom2 = new Room();
        newRoom2.setName("Bed Room");
        Room newRoom3 = new Room();
        newRoom2.setName("Room3");
        Room newRoom4 = new Room();
        newRoom2.setName("Room4");
        Room newRoom5 = new Room();
        newRoom2.setName("Room5");
        Integer roomId1 = userService.addRoom("mario2", newRoom1);
        Integer roomId2 = userService.addRoom("mario2", newRoom2);
        Integer roomId3 = userService.addRoom("mario2", newRoom3);
        Integer roomId4 = userService.addRoom("mario2", newRoom4);
        Integer roomId5 = userService.addRoom("mario2", newRoom5);
        roomService.addDevice(roomId1, DeviceType.DIMMABLE_LIGHT);
        roomService.addDevice(roomId1, DeviceType.LIGHT_SENSOR);
        roomService.addDevice(roomId2, DeviceType.HUMIDITY_SENSOR);
        roomService.addDevice(roomId3, DeviceType.MOTION_SENSOR);
        roomService.addDevice(roomId3, DeviceType.SMART_PLUG);
        roomService.addDevice(roomId3, DeviceType.STATELESS_DIMMABLE_SWITCH);
        roomService.addDevice(roomId3, DeviceType.TEMP_SENSOR);

        //ROOM4 is empty
        roomService.addDevice(roomId5, DeviceType.DIMMABLE_LIGHT);
    }


    //user with 20 rooms and random devices in them
    private void randUser() {
        User newuUser = new User("rand@usi.ch", "1234", "rand", "randomUser");
        userService.insert(newuUser);
        for (int i = 0; i < 20; i++) {
            Room newRoom = new Room();
            newRoom.setName(UUID.randomUUID().toString());
            Integer roomId = userService.addRoom("rand", newRoom);
            Random rand = new Random();

            int devices = rand.nextInt(30);
            for (i = 0; i < devices; i++) {
                DeviceType dt = DeviceType.intToDeviceType(rand.nextInt(9) + 1);
                roomService.addDevice(roomId, dt);
            }

        }
    }

    /**
     * When called adds various users to the storage
     */
    public void dummy1() {
        user1();
    }

    /**
     * Adds a User with key rand with randomly generated rooms and devices
     */
    public void randDummy() {
        randUser();
    }

}
