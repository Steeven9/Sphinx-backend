package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.misc.DeviceFactory;
import ch.usi.inf.sa4.sphinx.misc.DeviceType;
import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.Room;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Room service.
 * It has methods to interact with Room entities.
 * In general it implements a layer of abstraction over the storage.
 * @see Room
 */
@Service
@Transactional
public class RoomService {

    @Autowired
    RoomStorage roomStorage;
    @Autowired
    DeviceStorage deviceStorage;
    @Autowired
    CouplingService couplingService;
    @Autowired
    DeviceService deviceService;



    /**
     * Getter for a room.
     *
     * @param roomId the roomId
     * @return Returns the Room with the given Id if present in the storage
     */
    public Optional<Room> get(final Integer roomId) {
        return roomStorage.findById(roomId);
    }


    /**
     * Given a room, return all the devices in this room.
     *
     * @param roomId the id of the room
     * @return a list of all devices in this room
     */
    public Optional<List<Device>> getPopulatedDevices(final Integer roomId) {
        return roomStorage.findById(roomId).map(Room::getDevices);
    }

    /**
     * Updates the given user, the username is used to find the User and the given User to update its fields.
     *
     * @param room the room to update
     * @return true if successful update else false
     */
    public boolean update(@NonNull final Room room) {
        if (room.getId() == null || !roomStorage.existsById(room.getId())) {
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
    public Optional<Integer> addDevice(@NonNull final Integer roomId, @NonNull final DeviceType deviceType) {
        final Device newDevice = DeviceFactory.makeDevice(deviceType);
        if (newDevice == null) return Optional.empty();


        return roomStorage.findById(roomId).map(r -> {
                    r.addDevice(newDevice);
                    return deviceStorage.save(newDevice).getId();
                }

        );
    }


    /**
     * Deletes the device with the given Id from the room with the given Id.
     *
     * @param roomId   the id of the room
     * @param deviceId the id of the device
     * @return true if success else false
     */
    public boolean removeDevice(@NonNull final Integer roomId,@NonNull final Integer deviceId) {
        final Optional<Device> device = deviceService.get(deviceId);
        if (device.isEmpty()) return false;
        return roomStorage.findById(roomId).map(room -> {
            room.removeDevice(device.get());
            update(room);
            return Boolean.TRUE;
        }).orElse(Boolean.FALSE);
    }

}
