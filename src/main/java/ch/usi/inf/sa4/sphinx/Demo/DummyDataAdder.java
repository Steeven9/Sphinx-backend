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
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;


/**
 * Directly inserts users into the database
 */
@Component
//@Transactional
public class DummyDataAdder {


    @Autowired
    private UserService userService;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private RoomService roomService;
    private static final Logger logger = LoggerFactory.getLogger(DummyDataAdder.class);


    //ALL OF THEM SHOULD THROW IF FAILING SO JUST USE get() WITH THE OPTIONALS

//    @PostConstruct
    private void deleteUsers(){
        userService.delete("user1");
        userService.delete("user2");
        userService.delete("randUser");
        userService.delete("emptyUser");
        userService.delete("unverifiedUser");
    }


    /**
     * adds a User called user1 into storage this user has 1 Device Light in its "room1", the user has
     * sessionToken="user1SessionToken"
     */
    public void user1() {
        try {

            final User newUser = new User("mario@smarthut.xyz", "1234", "user1", "mario rossi");
            newUser.setVerified(true);
            newUser.setSessionToken("user1SessionToken");
            final boolean inserted = userService.insert(newUser);

            if (inserted) logger.info("user1 added to storage");

            final Room newRoom1 = new Room();
            newRoom1.setName("room1");
            final Room newRoom2 = new Room();
            newRoom2.setName("room2");
            final Integer roomId1 = userService.addRoom("user1", newRoom1).get();//leave roomId1 for debugging
            final Integer roomId2 = userService.addRoom("user1", newRoom2).get();
            final var room = roomService.get(roomId1);
            final var rooms = userService.get("user1").get().getRooms();

            roomService.addDevice(roomId1, DeviceType.LIGHT);
        } catch (final RuntimeException e) {
            logger.warn("SOMETHING IS WRONG IN user1");
            e.printStackTrace();
        }
    }



    /**
     * adds a User called user2 into storage with 5 rooms one of which is empty. This user owns all types of devices
     */
    public void user2() {
        try {
            final User newUser = new User("luigi@smarthut.xyz", "1234", "user2", "luigi rossi");
            newUser.setVerified(true);
            newUser.setSessionToken("user2SessionToken");
            final boolean inserted = userService.insert(newUser);
            if (inserted) logger.info("user2 added to storage");

            final Room newRoom1 = new Room();
            newRoom1.setName("Living Room");
            final Room newRoom2 = new Room();
            newRoom2.setName("Bed Room");
            final Room newRoom3 = new Room();
            newRoom2.setName("Room3");
            final Room newRoom4 = new Room();
            newRoom2.setName("Room4");
            final Room newRoom5 = new Room();
            newRoom2.setName("Room5");
            final Integer roomId1 = userService.addRoom("user2", newRoom1).get();
            final Integer roomId2 = userService.addRoom("user2", newRoom2).get();
            final Integer roomId3 = userService.addRoom("user2", newRoom3).get();
            final Integer roomId4 = userService.addRoom("user2", newRoom4).get();
            final Integer roomId5 = userService.addRoom("user2", newRoom5).get();
            final Optional<Integer> device1Id = roomService.addDevice(roomId1, DeviceType.DIMMABLE_LIGHT);
            roomService.addDevice(roomId1, DeviceType.LIGHT_SENSOR);
            roomService.addDevice(roomId2, DeviceType.HUMIDITY_SENSOR);
            final Optional<Integer> deviceId2 = roomService.addDevice(roomId3, DeviceType.MOTION_SENSOR);
            final Integer ownerRoomId = deviceService.get(deviceId2.get()).get().getRoom().getId();

            roomService.addDevice(roomId3, DeviceType.SMART_PLUG);
            roomService.addDevice(roomId3, DeviceType.STATELESS_DIMMABLE_SWITCH);
            roomService.addDevice(roomId3, DeviceType.TEMP_SENSOR);
            roomService.addDevice(roomId1, DeviceType.DIMMABLE_LIGHT);
            roomService.addDevice(roomId1, DeviceType.LIGHT_SENSOR);
            roomService.addDevice(roomId2, DeviceType.LIGHT);

            //ROOM4 is empty
            roomService.addDevice(roomId5, DeviceType.DIMMABLE_LIGHT);
        } catch (final RuntimeException e) {
            logger.warn("Something is wrong in user2");
        }
    }


    /**
     * adds a user called randUser to storage that has randomly filled rooms, this user has no session token and must be
     * logged in
     */
    //user with 20 rooms and random devices in them
    public void randUser() {
        try {
            final User newUser = new User("rand@smarthut.xyz", "1234", "randUser", "randomUser");
            newUser.setVerified(true);
            userService.insert(newUser);
            for (int i = 0; i < 20; i++) {
                final Room newRoom = new Room();
                newRoom.setName(UUID.randomUUID().toString());
                final Integer roomId = userService.addRoom("randUser", newRoom).get();
                final Random rand = new Random();

                final int devices = rand.nextInt(30);
                for (i = 0; i < devices; i++) {
                    final DeviceType dt = DeviceType.intToDeviceType(rand.nextInt(9) + 1);
                    roomService.addDevice(roomId, dt);
                }


            }
        } catch (final RuntimeException e) {
            logger.warn("Something is wrong in randUser");
        }
    }

    /**
     * adds a user called emptyUser in storage, it will have no rooms. This user has
     * sessionToken="emptyUserSessionToken"
     */
    public void emptyUser() {
        try {
            final User newUser = new User("empty@smarthut.xyz", "1234", "emptyUser", "Empty User");
            newUser.setVerified(true);
            newUser.setSessionToken("emptyUserSessionToken");
            if (userService.insert(newUser)) logger.info("emptyUser added to storage");
        } catch (final RuntimeException e) {
            logger.warn("Something is wrong in emptyUser");
        }
    }

    /**
     * adds an unverified user called unverifiedUser in storage
     */
    public void unverifiedUser() {
        try {
            final User newUser = new User("unv@smarthut.xyz", "1234", "unverifiedUser", "edeefefefef");
            if (userService.insert(newUser)) logger.info("unverifiedUser added to storage");
        } catch (final RuntimeException e) {
            logger.warn("Something is wrong in unverifiedUser");
        }
    }

}
