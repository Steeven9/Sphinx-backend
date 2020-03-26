package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.misc.DeviceType;
import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
     * @param deviceId the roomId
     * @return Returns the Room with the given Id if present in the storage
     */
    public final Room get(final Integer deviceId) {
        return roomStorage.get(deviceId);
    }


    /**
     * Given a room, return all the devices in this room.
     *
     * @param roomId the id of the room
     * @return a list of all devices in this room
     */
    public final List<Device> getPopulatedDevices(final Integer roomId) {
        Room room = roomStorage.get(roomId);
        if (room != null) {
            return room.getDevices().stream().map(deviceStorage::get).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    /**
     * Updates the given user, the username is used to find the User and the given User to update its fields
     *
     * @param room the room to update
     * @return true if successful update else false
     */
    public final boolean update(final Room room) {
        return roomStorage.update(room);
    }

    /**
     * Adds the type of device specified by deviceType to the Room with the given roomId
     *
     * @param roomId     id of the Room
     * @param deviceType the type of Device (ex DimmableLight)
     * @return the id of the device or null if it fails
     */
    public final Integer addDevice(final Integer roomId, DeviceType deviceType) {
        Device newDevice = DeviceType.makeDevice(deviceType);
        Room room = roomStorage.get(roomId);

        return deviceStorage.insert(newDevice);
    }


    /**
     * Deletes the device with the given Id from the room with the given Id
     *
     * @param roomId   the id of the room
     * @param deviceId the id of the device
     * @return true if succes else false
     */
    public final boolean removeDevice(final Integer roomId, final Integer deviceId) {
        Room room = roomStorage.get(roomId);
        if (room == null) {
            return false;
        }
        if (!room.getDevices().remove(deviceId)) {
            return false;
        }
        roomStorage.update(room);
        deviceStorage.delete(deviceId);
        return true;
    }


}