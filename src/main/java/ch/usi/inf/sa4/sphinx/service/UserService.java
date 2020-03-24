package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.Room;
import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


/*
 * Service layer of the application, the various Storage follows the CRUD principle
 */
@Service
public final class UserService {
//    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private  UserStorage userStorage;
    @Autowired
    private  RoomStorage roomStorage;
    @Autowired
    private  RoomService roomService;

    //Will be used to check that each room belongs to a single user
    private static final HashMap<String, String> roomToUser = new HashMap<>();


    /**
     *
     */
    //Used with spring injection can't be instantiated directly
    private UserService() {
    }


    /**
     * @param username the username
     * @return Returns the User with the given name if present in the storage
     */
    public User get(final String username){
        return userStorage.get(username);
    }


    public User getByMail(final String email){
        return userStorage.getByEmail(email);
    }


    /**
     * Deletes the user with the given Username from storage
     * @param username username of the user to delete
     */
    public  void delete(final String username) {
        userStorage.delete(username);
    }


    /**
     * Saves the given User in storage
     * @param user the User to save
     * @return true if success else false
     */
    public  boolean insert(final User user) {
        return userStorage.insert(user) == null;
    }

    /**
     * Updates the given user, the username is used to find the User and the given User to update its fields
     * @param username  the username of the User to update
     * @param user the User with updated fields
     * @return true if successful update else false
     */
    public boolean update(final String username, final User user){
        User oldUser = userStorage.get(username);
        if(oldUser != null){
            //Must be done manually to prevent corruption of list of owned rooms //EX: a roomId could be added to
            //User but not actually have a matching Id in the Storage. To prevent this we only update the list of
            //owned Rooms of a User in Storage from addRoom and removeRoom
            oldUser.setUsername(user.getUsername());
            oldUser.setEmail(user.getEmail());
            oldUser.setFullname(user.getFullname());
            oldUser.setPassword(user.getPassword());
            return  userStorage.update(username, oldUser);
        }
        return false;
    }


    /**
     * Adds the given room to the User, notice that both will be saved in Storage. The id of the room is generated by
     * the storage
     * @param username username of the User
     * @param room the Room to be added to the User
     * @return true if success else false
     */
    public boolean addRoom(final String username, final Room room) {
        final User user = userStorage.get(username);
        if (user == null) {
            return false;
        }

        var roomId = roomStorage.insert(room);
        if (roomId == null) {
            return false;
        }

        user.addRoom(roomId);
        return userStorage.update(username, user);
    }


    /**
     * @param username the name of the User whose room is to be removed
     * @param roomId the id of the room to remove
     */
    public void removeRoom(final String username, final Integer roomId){
        final User user = userStorage.get(username);
        user.removeRoom(roomId);
        userStorage.update(username, user);
    }


    /**
     * @param username username of the given User
     * @return Id of the Device(s) belonging to a given User
     */
    public List<Integer> getDevices(final String username) {
        var devices = new ArrayList<Integer>();
        final User user = userStorage.get(username);

        if (user != null) {
            var roomIds = user.getRooms();
            for(var roomId: roomIds){
                var r = roomService.get(roomId);
                devices.addAll(r.getDevices());
            }
        }
        return devices;
    }


    /**
     * @param username the username of the desired User
     * @param deviceId the id of the device
     * @return true if the User with the given Username owns the divice with the given Id
     */
    public boolean ownsDevice(String username, Integer deviceId){
        return getDevices(username).contains(deviceId);
    }



    /**
     * @param username the username of the desired User
     * @param roomId the id of the device
     * @return true if the User with the given Username owns the divice with the given Id
     */
    public boolean ownsRoom(String username, Integer roomId){
        User user = userStorage.get(username);
        if(user == null) return false;
        return user.getRooms().contains(roomId);
    }


    public List<Room> getPopulatedRooms(String username){
        User user = get(username);
        if(user != null){
            return user.getRooms().stream().map(roomStorage::get).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }


    /**
     * Returns a populated list of Device(s) owned by a given User
     * @param username username of required User
     * @return Devices owned by User
     */
    public List<Device> getPopulatedDevices(String username){
        User user = userStorage.get(username);
        if(user != null){
            return user.getRooms().stream().
                    flatMap(roomId -> roomService.getPopulatedDevices(roomId).stream()).
                    collect(Collectors.toList());
        }
        return new ArrayList<>();
    }


    /**
     * Checks if the given session token is a match to the one in Storage
     * @param username    the username of the User
     * @param sessionToken the session token
     * @return true if they match, false if the User does not exist or they don't match
     */
    public boolean validSession(String username, String sessionToken){
        User user = userStorage.get(username);
        if(user == null) return false;
        return user.getSessionToken().equals(sessionToken);
    }


    /**
     * Removes a device that the User owns
     * @param username the username whose device is to be removed
     * @param deviceId the id of the device to be removed
     */
    public void removeDevice(String username, Integer deviceId){
        User user = userStorage.get(username);
        if(user == null) return;

        List<Room> rooms = user.getRooms().stream().map(roomStorage::get).collect(Collectors.toList());

        for(Room room: rooms){
            if(room.getDevices().contains(deviceId)){
                roomService.removeDevice(room.getId(), deviceId);
            }
        }
    }




}
