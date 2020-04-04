package ch.usi.inf.sa4.sphinx.Demo;


import ch.usi.inf.sa4.sphinx.misc.DeviceType;
import ch.usi.inf.sa4.sphinx.model.Room;
import ch.usi.inf.sa4.sphinx.model.User;
import ch.usi.inf.sa4.sphinx.service.DeviceService;
import ch.usi.inf.sa4.sphinx.service.RoomService;
import ch.usi.inf.sa4.sphinx.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.UUID;


/**
 *
 */
@Component
public class DummyDataAdder {


    @Autowired
    private UserService userService;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private RoomService roomService;
    private static Logger logger = LoggerFactory.getLogger(DummyDataAdder.class);



    /**
     * adds a User called user1 into storage this user has 1 Device Light in its "room1", the user has
     * sessionToken="user1SessionToken"
     */
    public void user1() {
        User newUser = new User("mario@usi.ch", "1234", "user1", "mario rossi");
        newUser.setVerified(true);
        newUser.setSessionToken("user1SessionToken");
        boolean inserted = userService.insert(newUser);

        if(inserted) logger.info("user1 added to storage");

        Room newRoom1 = new Room();
        newRoom1.setName("room1");
        Room newRoom2 = new Room();
        newRoom2.setName("room2");
        Integer roomId1 = userService.addRoom("user1", newRoom1);//leave roomId1 for debugging
        Integer roomId2 = userService.addRoom("user1", newRoom2);
        roomService.addDevice(roomId1, DeviceType.LIGHT);
    }


    /**
     * adds a User called user2 into storage with 5 rooms one of which is empty. This user owns all types of devices
     */
    public void user2() {
        User newUser = new User("mario2@usi.ch", "1234", "user2", "mario rossi");
        newUser.setVerified(true);
        newUser.setSessionToken("user2SessionToken");
        boolean inserted  = userService.insert(newUser);
        if(inserted) logger.info("user2 added to storage");

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
        Integer roomId1 = userService.addRoom("user2", newRoom1);
        Integer roomId2 = userService.addRoom("user2", newRoom2);
        Integer roomId3 = userService.addRoom("user2", newRoom3);
        Integer roomId4 = userService.addRoom("user2", newRoom4);
        Integer roomId5 = userService.addRoom("user2", newRoom5);
        roomService.addDevice(roomId1, DeviceType.DIMMABLE_LIGHT);
        roomService.addDevice(roomId1, DeviceType.LIGHT_SENSOR);
        roomService.addDevice(roomId2, DeviceType.HUMIDITY_SENSOR);
        roomService.addDevice(roomId3, DeviceType.MOTION_SENSOR);
        roomService.addDevice(roomId3, DeviceType.SMART_PLUG);
        roomService.addDevice(roomId3, DeviceType.STATELESS_DIMMABLE_SWITCH);
        roomService.addDevice(roomId3, DeviceType.TEMP_SENSOR);
        roomService.addDevice(roomId1, DeviceType.DIMMABLE_LIGHT);
        roomService.addDevice(roomId1, DeviceType.LIGHT_SENSOR);
        roomService.addDevice(roomId2, DeviceType.LIGHT);

        //ROOM4 is empty
        roomService.addDevice(roomId5, DeviceType.DIMMABLE_LIGHT);
    }


    /**
     * adds a user called randUser to storage that has randomly filled rooms, this user has no session token and must be
     * logged in
     */
    //user with 20 rooms and random devices in them
    public void randUser() {
        User newUser = new User("rand@usi.ch", "1234", "randUser", "randomUser");
        newUser.setVerified(true);
        userService.insert(newUser);
        for (int i = 0; i < 20; i++) {
            Room newRoom = new Room();
            newRoom.setName(UUID.randomUUID().toString());
            Integer roomId = userService.addRoom("randUser", newRoom);
            Random rand = new Random();

            int devices = rand.nextInt(30);
            for (i = 0; i < devices; i++) {
                DeviceType dt = DeviceType.intToDeviceType(rand.nextInt(9) + 1);
                roomService.addDevice(roomId, dt);
            }

        }
    }

    /**
     * adds a user called emptyUser in storage, it will have no rooms. This user has
     * sessionToken="emptyUserSessionToken"
     */
    public void emptyUser() {
        User newUser = new User("rand@usi.ch", "1234", "emptyUser", "randomUser");
        newUser.setVerified(true);
        newUser.setSessionToken("emptyUserSessionToken");
        if(userService.insert(newUser)) logger.info("emptyUser added to storage");
    }

    /**
     * adds an unverified user called unverifiedUser in storage
     */
    public void unverifiedUser(){
        User newUser = new User("unv@usi.ch", "1234", "unverifiedUser", "edeefefefef");
        if(userService.insert(newUser)) logger.info("unverifiedUser added to storage");
    }


}
