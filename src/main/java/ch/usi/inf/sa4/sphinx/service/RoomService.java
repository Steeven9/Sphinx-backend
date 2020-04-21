package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.misc.DeviceType;
import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

/**
 *
 */
@Service
public class RoomService {

    @Autowired
    RoomStorage roomStorage;
    @Autowired
    DeviceStorage deviceStorage;


    /**
     * Getter for a room.
     *
     * @param deviceId the roomId
     * @return Returns the Room with the given Id if present in the storage
     */
    public final Optional<Room> get(final Integer deviceId) {
        return roomStorage.findById(deviceId);
    }


    /**
     * Given a room, return all the devices in this room.
     *
     * @param roomId the id of the room
     * @return a list of all devices in this room
     */
    public final Optional<List<Device>> getPopulatedDevices(final Integer roomId) {
        return roomStorage.findById(roomId).map(Room::getDevices);
    }

    /**
     * Updates the given user, the username is used to find the User and the given User to update its fields.
     *
     * @param room the room to update
     * @return true if successful update else false
     */
    public final boolean update(@NotNull final Room room) {
        if (!roomStorage.existsById(room.getId())) {
            return false;
        }

        roomStorage.save(room);
        return true;
    }

    /**
     * Adds the type of device specified by deviceType to the Room with the given roomId.
     *
     * @param roomId     id of the Room
     * @param deviceType the type of Device (ex DimmableLight)
     * @return the id of the device or null if it fails
     */
    public final Optional<Integer> addDevice(@NotNull final Integer roomId, @NotNull DeviceType deviceType) {
        Device newDevice = DeviceType.makeDevice(deviceType);
        if (newDevice == null) return Optional.empty();

        return roomStorage.findById(roomId).map(r -> {
                    newDevice.setRoom(r);
                    return deviceStorage.save(newDevice).getId();
                }

        );
    }


    /**
     * Deletes the device with the given Id from the room with the given Id.
     *
     * @param roomId   the id of the room
     * @param deviceId the id of the device
     * @return true if succes else false
     */
    public final boolean removeDevice(final Integer roomId, final Integer deviceId) {
        deviceStorage.deleteById(deviceId);
        return true;
    }


}
