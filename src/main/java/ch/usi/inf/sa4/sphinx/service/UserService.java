package ch.usi.inf.sa4.sphinx.service;


import ch.usi.inf.sa4.sphinx.misc.DeviceType;
import ch.usi.inf.sa4.sphinx.misc.ImproperImplementationException;
import ch.usi.inf.sa4.sphinx.misc.UnauthorizedException;
import ch.usi.inf.sa4.sphinx.misc.WrongUniverseException;
import ch.usi.inf.sa4.sphinx.model.*;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * User service.
 * It has methods to interact with User entities.
 * In general it implements a layer of abstraction over the storage.
 *
 * @see User
 */
@Service
public class UserService {
    @Autowired
    private UserStorage userStorage;
    @Autowired
    private RoomStorage roomStorage;
    @Autowired
    private DeviceStorage deviceStorage;

    /**
     * @deprecated Do not use directly this constructor
     */
    //Needed public otherwise context creation will fail...
    @Deprecated(forRemoval = false)
    public UserService() {
        // JPA needs a default constructor.
    }


    /**
     * getter for User
     *
     * @param username the username
     * @return Returns the User with the given name if present in the storage
     */
    public Optional<User> get(final String username) {
        return userStorage.findByUsername(username);
    }


    /**
     * gets a User by mail {@code email} from storage
     *
     * @param email email of the user
     * @return the user with the given email or null if not found
     */
    public Optional<User> getByMail(final String email) {
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
        if (user.getId() != null || user.getPassword() == null) return false;
        user.createResetCode();
        userStorage.save(user);
        return true;
    }


    /**
     * Updates the given user, the username is used to find the User and the given User to update its fields
     *
     * @param user the User with updated fields
     * @return true if successful update else false
     */
    public boolean update(@NonNull final User user) {
        if (!userStorage.existsById(user.getId())) return false;

        userStorage.save(user);
        return true;
    }


    /**
     * Adds the given room to the User, notice that both will be saved in Storage. The id of the room is generated by
     * the storage and returned.
     *
     * @param username username of the User
     * @param room     the Room to be added to the User
     * @return the id of the room
     */
    public Optional<Integer> addRoom(final String username, final Room room) {
        final Optional<User> user = userStorage.findByUsername(username);

        return user.map(u -> {
            room.setUser(u);
            return roomStorage.save(room).getId();
        });
    }


    /**
     * Removes a given room.
     *
     * @param username the name of the User whose room is to be removed
     * @param roomId   the id of the room to remove
     * @return true if the operation succeeds else false
     */
    public boolean removeRoom(final String username, final Integer roomId) {
        if (!ownsRoom(username, roomId)) {
            return false;
        }
//

        final Optional<User> user = userStorage.findByUsername(username);
        user.ifPresent(
                u -> {
                    u.removeRoom(roomId);
                    userStorage.save(u);
                }
        );
        return user.isPresent();
    }


    /**
     * Getter for device(s).
     *
     * @param username username of the given User
     * @return Id of the Device(s) belonging to a given User
     */
    public Optional<List<Integer>> getDevices(final String username) {
        return getPopulatedDevices(username).map(devices ->
                devices.stream().map(Device::getId).collect(Collectors.toList())
        );
    }


    /**
     * Assert ownership of a Device
     *
     * @param username the username of the desired User
     * @param deviceId the id of the device
     * @return true if the User with the given Username owns the divice with the given Id
     */
    public boolean ownsDevice(final String username, final Integer deviceId) {
        return getDevices(username).map(ids -> ids.stream().anyMatch(id -> id.equals(deviceId))).orElse(Boolean.FALSE);
    }


    /**
     * Returns a list of rooms of this user.
     *
     * @param username User of these/this room/s
     * @return a list of rooms
     */
    public List<Room> getPopulatedRooms(final String username) {
        return userStorage.findByUsername(username).map(User::getRooms).orElseGet(ArrayList::new);
    }

    /**
     * Assert ownership of a room
     *
     * @param username the username of the desired User
     * @param roomId   the id of the room
     * @return true if the User with the given Username owns the room with the given Id, else false
     */
    public boolean ownsRoom(@NonNull final String username, final Integer roomId) {
        return userStorage.findByUsername(username)
                .map(user -> user.getRooms().stream().anyMatch(r -> r.getId().equals(roomId)))
                .orElse(Boolean.FALSE);
    }


    /**
     * Returns a populated list of Device(s) owned by a given User
     *
     * @param username username of required User
     * @return Devices owned by User
     */
    public Optional<List<Device>> getPopulatedDevices(final String username) {
        return userStorage.findByUsername(username)
                .map(u -> u.getRooms().stream().flatMap(
                        r -> r.getDevices().stream()).collect(Collectors.toList()));
    }

    /**
     * Checks if there exists a valid session with the given username and sessionToken.
     * Throws an UnauthorisedException if not.
     * @param username the username to authenticate as
     * @param sessionToken the session token of the user
     * @throws UnauthorizedException if session is invalid or user does not exist
     */
    public void validateSession(@NonNull final String username, @NonNull final String sessionToken) {
        final Optional<Boolean> foundMatch = userStorage.findByUsername(username)
                .map(user -> sessionToken.equals(user.getSessionToken()));
        // Java sucks and sonarqube is a mess, therefore we need to write this shit.
        // Do not refactor back to !foundMatch.get()
        if (foundMatch.isEmpty() || Boolean.FALSE.equals(foundMatch.get())) {
            throw new UnauthorizedException("Invalid credentials");
        }
    }


    /**
     * Removes a device that the User owns
     *
     * @param username the username whose device is to be removed
     * @param deviceId the id of the device to be removed
     */
    public void removeDevice(final String username, final Integer deviceId) {
        if (ownsDevice(username, deviceId)) {
            deviceStorage.deleteById(deviceId);
        }
    }


    public Optional<User> getById(final Integer id) {
        return userStorage.findById(id);
    }


    /**
     * Assert if Device has been moved
     *
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

        final Room startRoom = roomStorage.findById(startRoomId)
                .orElseThrow(WrongUniverseException::new);

        if (!startRoom.getDevicesIds().contains(deviceId)) {
            return false;
        }

        return roomStorage.findById(endRoomId).map(room -> deviceStorage.findById(deviceId).map(device -> {
            device.setRoom(room);
            deviceStorage.save(device);
            return Boolean.TRUE;
        }).orElse(Boolean.FALSE)).orElse(Boolean.FALSE);
    }

    /**
     * Assert ownership of a room
     * returns the id of the room that owns the device given a the owning user and the room id
     *
     * @param username username of the room owner
     * @param deviceId id of the owned device
     * @return the id of the room containing the device
     */
    public Integer owningRoom(final String username, final Integer deviceId) {
        final var rooms = getPopulatedRooms(username);
        for (final Room r : rooms) {
            if (r.getDevicesIds().contains(deviceId)) {
                return r.getId();
            }
        }
        return null;
    }

    /**
     * Changes the name of a User identified by {@code oldUsername}
     *
     * @param oldUsername the old name of the user
     * @param newUsername the new name of the user
     * @return true if successful else false
     */
    public boolean changeUsername(@NonNull final String oldUsername, @NonNull final String newUsername) {
        try {
            return userStorage.findByUsername(oldUsername).map(user -> {
                user.setUsername(newUsername);
                userStorage.save(user);
                return Boolean.TRUE;
            }).orElse(Boolean.FALSE);
        } catch (final ConstraintViolationException e) {
            return false;
        }
    }



    //returns the hashed password of a user
    private Optional<String> getUserHash(@NonNull String username) {
        return get(username).map(User::getPassword);
    }


    /**
     * Updates values of all sensors of a given user.
     *
     * @param username owner of all devices
     */
    public void generateValue(String username) {
        Optional<List<Device>> optionalDevices = this.getPopulatedDevices(username);
        if (optionalDevices.isPresent()) {
            List<Device> devices = optionalDevices.get();
            for (Device device : devices) {
                if (device instanceof Sensor) {
                    ((Sensor) device).generateValue(); //updates the value of every sensor
                    deviceStorage.save(device);
                }
            }
        }
    }
}
