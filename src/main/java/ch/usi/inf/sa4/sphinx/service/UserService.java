package ch.usi.inf.sa4.sphinx.service;


import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.Room;
import ch.usi.inf.sa4.sphinx.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/*
 * Service layer of the application, the various Storage follows the CRUD principle
 */
@Service
public class UserService {
    @Autowired
    private UserStorage userStorage;
    @Autowired
    private RoomStorage roomStorage;
    @Autowired
    private RoomService roomService;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private DeviceStorage deviceStorage;

    //Will be used to check that each room belongs to a single user
    private static final HashMap<String, String> roomToUser = new HashMap<>();


    /**
     *
     */
    //Used with spring injection can't be instantiated directly
    public UserService() {
    }


    /**
     * getter for User
     * @param username the username
     * @return Returns the User with the given name if present in the storage
     */
    public User get(final String username) {
        return userStorage.findByUsername(username).orElse(null);
    }


    /** gets a User by mail {@param email} from storage
     * @param email email of the user
     * @return the user with the given email or null if not found
     */
    public User getByMail(final String email) {
        return userStorage.findByEmail(email);
    }


    /**
     * Deletes the user and its rooms and devices
     *
     * @param username username of the User that needs to be deleted
     */
    public void delete(final String username) {
        userStorage.deleteByUsername(username);
    }


    /**
     * Saves the given User in storage
     *
     * @param user the User to save
     * @return true if success else false
     */
    public boolean insert(final User user) {
        userStorage.save(user);
        return true;
    }


    /**
     * Updates the given user, the username is used to find the User and the given User to update its fields
     *
     * @param user     the User with updated fields
     * @return true if successful update else false
     */
    public boolean update(final User user) {
        if(userStorage.existsById(user.getId())){
            userStorage.save(user); //Now the newly added rooms will be inserted in storage by jpa
        }
        return false;
    }



    /**
     * Adds the given room to the User, notice that both will be saved in Storage. The id of the room is generated by
     * the storage and returned.
     *
     * @param username username of the User
     * @param room     the Room to be added to the User
     * @return the id of the room
     */
    public Integer addRoom(final String username, final Room room) {
        final Optional<User> user = userStorage.findByUsername(username);


        return user.map(u-> {
            u.addRoom(room);
            return userStorage.save(u).getId();
        }).orElse(null);
    }


    /**
     * Removes a given room.
     *
     * @param username the name of the User whose room is to be removed
     * @param roomId   the id of the room to remove
     */
    public boolean removeRoom(final String username, final Integer roomId) {
        if(!ownsRoom(username, roomId)){
            return false;
        }


        final Optional<User> user = userStorage.findByUsername(username);
        user.ifPresent(
                u->{
                    u.removeRoom(roomId);
                    userStorage.save(u);
                }
        );
        return true;
    }


    /**
     * Getter for device(s).
     * @param username username of the given User
     * @return Id of the Device(s) belonging to a given User
     */
    public List<Integer> getDevices(final String username) {
        return userStorage.findByUsername(username).map(
                user -> user.getRooms().stream().map(Room::getId).collect(Collectors.toList())
        ).orElse(new ArrayList<>());

    }


    /**
     * Assert ownership of a Device
     * @param username the username of the desired User
     * @param deviceId the id of the device
     * @return true if the User with the given Username owns the divice with the given Id
     */
    public boolean ownsDevice(String username, Integer deviceId) {
        return getDevices(username).contains(deviceId);
    }


    /**
     * Returns a list of rooms of this user.
     *
     * @param username User of these/this room/s
     * @return a list of rooms
     */
    public List<Room> getPopulatedRooms(String username) {
        return userStorage.findByUsername(username).map(User::getRooms).orElse(new ArrayList<>());
    }

    /**
     * Assert ownership of a room
     * @param username the username of the desired User
     * @param roomId   the id of the room
     * @return true if the User with the given Username owns the room with the given Id
     */
    public boolean ownsRoom(String username, Integer roomId) {
       return userStorage.findByUsername(username)
               .map(user -> user.getRooms().stream().anyMatch(r->r.getId().equals(roomId)))
               .orElse(false);

    }


    /**
     * Returns a populated list of Device(s) owned by a given User
     *
     * @param username username of required User
     * @return Devices owned by User
     */
    public List<Device> getPopulatedDevices(String username) {
        return userStorage.findByUsername(username)
                .map(
                        u->u.getRooms().stream().flatMap(
                                r->r.getDevices().stream()).collect(Collectors.toList()))
                .orElse(new ArrayList<>());

    }


    /**
     * Checks if the given session token is a match to the one in Storage
     *
     * @param username     the username of the User
     * @param sessionToken the session token
     * @return true if they match, false if the User does not exist or they don't match
     */
    public boolean validSession(@NotNull String username, String sessionToken) {
        return userStorage.findByUsername(username)
                .map(user -> user.getSessionToken().equals(sessionToken)).orElse(false);
    }


    /**
     * Removes a device that the User owns
     *
     * @param username the username whose device is to be removed
     * @param deviceId the id of the device to be removed
     */
    public void removeDevice(String username, Integer deviceId) {
        if(ownsDevice(username, deviceId)){
            deviceStorage.deleteById(deviceId);
        }
    }


    /**
     * Assert if Device has been moved
     * @param username    the owner of the device and rooms
     * @param deviceId    the id of the device to Migrate
     * @param startRoomId the room the device sits in
     * @param endRoomId   the room the device should end in
     * @return true if success else false
     */
    @Transactional
    public boolean migrateDevice(final String username, final Integer deviceId, final Integer startRoomId,
                                 final Integer endRoomId) {

        if (!ownsRoom(username, startRoomId) || !ownsRoom(username, endRoomId) || !ownsDevice(username, deviceId)) {
            return false;
        }

        roomStorage.findById(endRoomId).ifPresent(room -> {
            deviceStorage.findById(deviceId).ifPresent(device -> {
                device.setRoom(room);
                deviceStorage.save(device);
            });

        });

        return true;
    }

    /**
     *  Assert ownership of a room
     * returns the id of the room that owns the device given a the owning user and the room id
     * @param username username of the room owner
     * @param deviceId id of the owned device
     * @return the id of the room containing the device
     */
    public Integer owningRoom(final String username, final Integer deviceId){
        var rooms = getPopulatedRooms(username);
        for(Room r: rooms){
            if(r.getDevicesIds().contains(deviceId)){
                return r.getId();
            }
        }
        return null;
    }

    /**
     * Changes the name of a User identified by {@param oldUsername}
     * @param oldUsername  the old name of the user
     * @param newUsername the new name of the user
     * @return true if successful else false
     */
    public boolean changeUsername(@NotNull final  String oldUsername, @NotNull final String newUsername) {
        try {
            return userStorage.findByUsername(oldUsername).map(user -> {
                user.setUsername(newUsername);
                userStorage.save(user);
                return true;
            }).orElse(false);
        } catch (ConstraintViolationException e){
            return false;
        }
    }


}
