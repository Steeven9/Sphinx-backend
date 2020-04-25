package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.misc.DeviceType;
import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.Room;
import lombok.NonNull;
import ch.usi.inf.sa4.sphinx.model.TempSensor;
import ch.usi.inf.sa4.sphinx.model.Thermostat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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
    public final Optional<Room> get(final Integer roomId) {
        return roomStorage.findById(roomId);
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
    public final boolean update(@NonNull final Room room) {
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
    public final Optional<Integer> addDevice(@NonNull final Integer roomId, @NonNull DeviceType deviceType) {
        Device newDevice = DeviceType.makeDevice(deviceType);
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
     * @return true if succes else false
     */
    public final boolean removeDevice(@NonNull final Integer roomId,@NonNull final Integer deviceId) {
        try {
            deviceStorage.deleteById(deviceId);
        } catch (EmptyResultDataAccessException e){
            return false;
        }
        return true;
    }

    /**
     * Returns the average temperature from all temperature sensors and the thermostat in a given room.
     *
     * @param roomId       the id of the room
     * @param thermostatId the id og the thermostat
     * @return the average temperature
     */
    /*public double getAverageTemp(final Integer roomId, final Integer thermostatId) {
        List<Device> list = this.getPopulatedDevices(roomId);
        double averageTemp = 0.0, sensors = 0.0;

        for (Device device : list) {
            if (DeviceType.deviceToDeviceType(device) == DeviceType.TEMP_SENSOR) {
                averageTemp += ((TempSensor) device).getValue();
                sensors++;
            }
        }
        Thermostat thermostat = (Thermostat) deviceService.get(thermostatId);
        averageTemp += thermostat.getValue();

        if (sensors > 0) { // some sensors are detected
            ++sensors; // +1 thermostat
            averageTemp = averageTemp / sensors;
        }

        thermostat.setAverageTemp(averageTemp);
        deviceService.update(thermostat);
        return averageTemp;
    }*/

}
