package ch.usi.inf.sa4.sphinx.service;


import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.Room;
import ch.usi.inf.sa4.sphinx.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


/*
 * Service layer of the application, the various Storage follows the CRUD principle
 */
@Service
public final class UserService {
    @Autowired
    private VolatileUserStorage userStorage;
    @Autowired
    private Storage<Integer, Room> roomStorage;
    @Autowired
    private RoomService roomService;
    @Autowired
    private DeviceService deviceService;

    //Will be used to check that each room belongs to a single user
    private static final HashMap<String, String> roomToUser = new HashMap<>();


    /**
     *
     */
    //Used with spring injection can't be instantiated directly
    private UserService() {
    }


    /**
     * getter for User
     * @param username the username
     * @return Returns the User with the given name if present in the storage
     */
    public User get(final String username) {
        return userStorage.get(username);
    }


    /** gets a User by mail {@param email} from storage
     * @param email email of the user
     * @return the user with the given email or null if not found
     */
    public User getByMail(final String email) {
        return userStorage.getByEmail(email);
    }


    /**
     * Deletes the user with the given Username from storage
     *
     * @param username username of the user to delete
     */
    public void delete(final String username) {
        userStorage.delete(username);
    }


    /**
     * Saves the given User in storage
     *
     * @param user the User to save
     * @return true if success else false
     */
    public boolean insert(final User user) {
        return userStorage.insert(user) != null;
    }


    /**
     * Updates the given user, the username is used to find the User and the given User to update its fields
     *
     * @param user     the User with updated fields
     * @return true if successful update else false
     */
    public boolean update(final User user) {
        User oldUser = userStorage.get(user.getUsername());
        if (oldUser != null) {
            User newUser = new User(oldUser, user.getEmail(),
                    user.getPassword(),
                    user.getFullname(),
                    user.getResetCode(),
                    user.getSessionToken(),
                    user.isVerified());
            return userStorage.update(newUser);
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
        final User user = userStorage.get(username);
        if (user == null) {
            return null; //something went bad
        }

        var roomId = roomStorage.insert(room);
        if (roomId == null) {
            return null; //something went bad
        }
        user.addRoom(roomId);
        if (!userStorage.update(user)) {
            return null;

        }
        return roomId;
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

        final User user = userStorage.get(username);
        user.removeRoom(roomId);
        userStorage.update(user);
        roomStorage.delete(roomId);
        return true;
    }


    /**
     * Getter for device(s).
     * @param username username of the given User
     * @return Id of the Device(s) belonging to a given User
     */
    public List<Integer> getDevices(final String username) {
        var devices = new ArrayList<Integer>();
        final User user = userStorage.get(username);

        if (user != null) {
            var roomIds = user.getRooms();
            for (var roomId : roomIds) {
                var r = roomService.get(roomId);
                devices.addAll(r.getDevices());
            }
        }
        return devices;
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
        User user = get(username);
        if (user != null) {
            return user.getRooms().stream().map(roomStorage::get).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    /**
     * Assert ownership of a room
     * @param username the username of the desired User
     * @param roomId   the id of the device
     * @return true if the User with the given Username owns the divice with the given Id
     */
    public boolean ownsRoom(String username, Integer roomId) {
        User user = userStorage.get(username);
        if (user == null) return false;
        return user.getRooms().contains(roomId);
    }


    /**
     * Returns a populated list of Device(s) owned by a given User
     *
     * @param username username of required User
     * @return Devices owned by User
     */
    public List<Device> getPopulatedDevices(String username) {
        User user = userStorage.get(username);
        if (user != null) {
            return user.getRooms().stream().
                    flatMap(roomId -> roomService.getPopulatedDevices(roomId).stream()).
                    collect(Collectors.toList());
        }
        return new ArrayList<>();
    }


    /**
     * Checks if the given session token is a match to the one in Storage
     *
     * @param username     the username of the User
     * @param sessionToken the session token
     * @return true if they match, false if the User does not exist or they don't match
     */
    public boolean validSession(@NotNull String username, String sessionToken) {
        User user = userStorage.get(username);
        if (user == null) return false;
        return user.getSessionToken().equals(sessionToken);
    }


    /**
     * Removes a device that the User owns
     *
     * @param username the username whose device is to be removed
     * @param deviceId the id of the device to be removed
     */
    public void removeDevice(String username, Integer deviceId) {
        User user = userStorage.get(username);
        if (user == null) return;

        List<Room> rooms = user.getRooms().stream().map(roomStorage::get).collect(Collectors.toList());

        for (Room room : rooms) {
            if (room.getDevices().contains(deviceId)) {
                roomService.removeDevice(room.getId(), deviceId);
            }
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
    public boolean migrateDevice(final String username, final Integer deviceId, final Integer startRoomId,
                                 final Integer endRoomId) {

        if (!ownsRoom(username, startRoomId) || !ownsRoom(username, endRoomId) || !ownsDevice(username, deviceId)) {
            return false;
        }

        Device device = deviceService.get(deviceId);
        Room startRoom = roomService.get(startRoomId);
        Room endRoom = roomService.get(endRoomId);

        startRoom.getDevices().remove(deviceId);
        endRoom.getDevices().add(deviceId);

        //user service would block the update  of the list of devices so we need to go directly to the db
        roomStorage.update(startRoom);
        roomStorage.update(endRoom);
        return true;
    }

    /**
     *  Assert ownership of a room
     * returns the id of the room that owns the device given a the onwing user and the room id
     * @param username username of the room owner
     * @param deviceId id of the owned device
     * @return the id of the room containing the device
     */
    public Integer owningRoom(final String username, final Integer deviceId){
        var rooms = getPopulatedRooms(username);
        for(Room r: rooms){
            if(r.getDevices().contains(deviceId)){
                return r.getId();
            }
        }
        return null;
    }

    /**
     * Changes the name of a User identified by {@param oldusername}
     * @param oldUsername  the old name of the user
     * @param newUsername the new name of the user
     * @return true if successful else false
     */
    public boolean changeUsername(@NotNull final  String oldUsername, final String newUsername) {
        if(newUsername == null) return false;

        User oldUser = userStorage.get(oldUsername);
        if(oldUser == null) return false;
        oldUser.setUsername(newUsername);

        return true;
    }


}
