package ch.usi.inf.sa4.sphinx.demo;


import ch.usi.inf.sa4.sphinx.misc.DeviceType;
import ch.usi.inf.sa4.sphinx.misc.WrongUniverseException;
import ch.usi.inf.sa4.sphinx.model.Room;
import ch.usi.inf.sa4.sphinx.model.User;
import ch.usi.inf.sa4.sphinx.service.RoomService;
import ch.usi.inf.sa4.sphinx.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Random;
import java.util.UUID;


/**
 * Directly inserts users into the database
 */
@Component
public class DummyDataAdder {


    public static final  String USER1 = "user1";
    public static final  String USER2 = "user2";
    public static final  String RAND = "randUser";
    public static final  String EMPTY = "emptyUser";
    public static final  String UNVERIFIED = "unverifiedUser";

    @Autowired
    private UserService userService;
    @Autowired
    private RoomService roomService;
    private static final Logger logger = LoggerFactory.getLogger(DummyDataAdder.class);


    //ALL OF THEM SHOULD THROW IF FAILING SO JUST USE get() WITH THE OPTIONALS

    @PostConstruct
    private void deleteUsers(){
        userService.delete(USER1);
        userService.delete(USER2);
        userService.delete(RAND);
        userService.delete(EMPTY);
        userService.delete(UNVERIFIED);
    }


    /**
     * adds a User called user1 into storage this user has 1 Device Light in its "room1", the user has
     * sessionToken="user1SessionToken"
     */
    public void addUser1() {
        try {

            final User newUser = new User("mario@smarthut.xyz", "1234", USER1, "mario rossi");
            newUser.setVerified(true);
            newUser.setSessionToken("user1SessionToken");
            final boolean inserted = userService.insert(newUser);

            if (inserted) logger.info("user1 added to storage");

            final Room newRoom1 = new Room();
            newRoom1.setName("room1");
            final Room newRoom2 = new Room();
            newRoom2.setName("room2");
            final Integer roomId1 = userService.addRoom(USER1, newRoom1).orElseThrow(WrongUniverseException::new);//leave roomId1 for debugging
            userService.addRoom(USER1, newRoom2);

            roomService.addDevice(roomId1, DeviceType.LIGHT);
        } catch (final RuntimeException e) {
            logger.warn("SOMETHING IS WRONG IN user1", e);
        }
    }



    /**
     * adds a User called user2 into storage with 5 rooms one of which is empty. This user owns all types of devices
     */
    public void addUser2() {
        try {
            final User newUser = new User("luigi@smarthut.xyz", "1234", USER2, "luigi rossi");
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
            final Integer roomId1 = userService.addRoom(USER2, newRoom1).orElseThrow(WrongUniverseException::new);
            final Integer roomId2 = userService.addRoom(USER2, newRoom2).orElseThrow(WrongUniverseException::new);
            final Integer roomId3 = userService.addRoom(USER2, newRoom3).orElseThrow(WrongUniverseException::new);
            userService.addRoom(USER2, newRoom4);
            final Integer roomId5 = userService.addRoom(USER2, newRoom5).orElseThrow(WrongUniverseException::new);
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
        } catch (final RuntimeException e) {
            logger.warn("Something is wrong in user2");
        }
    }

    // This is only here to make SonarQube shut up. The method is only called once so it really doesn't hurt to make
    // this a local.
    private final Random random = new Random();
    /**
     * adds a user called randUser to storage that has randomly filled rooms, this user has no session token and must be
     * logged in
     */
    public void addRandUser() {
        try {
            final User newUser = new User("rand@smarthut.xyz", "1234", RAND, "randomUser");
            newUser.setVerified(true);
            userService.insert(newUser);
            for (int i = 0; i < 20; i++) {
                final Room newRoom = new Room();
                newRoom.setName(UUID.randomUUID().toString());
                final Integer roomId = userService.addRoom(RAND, newRoom).orElseThrow(WrongUniverseException::new);

                final int devices = random.nextInt(30);
                for (int j = 0; j < devices; j++) {
                    final DeviceType dt = DeviceType.intToDeviceType(random.nextInt(9) + 1);
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
    public void addEmptyUser() {
        try {
            final User newUser = new User("empty@smarthut.xyz", "1234", EMPTY, "Empty User");
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
    public void addUnverifiedUser() {
        try {
            final User newUser = new User("unv@smarthut.xyz", "1234", UNVERIFIED, "edeefefefef");
            if (userService.insert(newUser)) logger.info("unverifiedUser added to storage");
        } catch (final RuntimeException e) {
            logger.warn("Something is wrong in unverifiedUser");
        }
    }

    /**
     * Adds all dummyData users
     */
    public void addDummyData(){
        addEmptyUser();
        addRandUser();
        addUser1();
        addUser2();
        addUnverifiedUser();
    }

}
