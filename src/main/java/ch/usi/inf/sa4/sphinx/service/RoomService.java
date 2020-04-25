package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.misc.DeviceType;
import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.Room;
import ch.usi.inf.sa4.sphinx.model.TempSensor;
import ch.usi.inf.sa4.sphinx.model.Thermostat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
@Service
public class RoomService {

    @Autowired
    Storage<Integer, Room> roomStorage;
    @Autowired
    Storage<Integer, Device> deviceStorage;
    @Autowired
    CouplingService couplingService;
    @Autowired
    DeviceService deviceService;



    /**
     * Getter for a room.
     * @param roomId the roomId
     * @return Returns the Room with the given Id if present in the storage
     */
    public final Room get(final Integer roomId) {
        return roomStorage.get(roomId);
    }


    /**
     * Given a room, return all the devices in this room.
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
     * Updates the given user, the username is used to find the User and the given User to update its fields.
     * @param room the room to update
     * @return true if successful update else false
     */
    public final boolean update(final Room room) {
        return roomStorage.update(room);
    }

    /**
     * Adds the type of device specified by deviceType to the Room with the given roomId.
     * @param roomId     id of the Room
     * @param deviceType the type of Device (ex DimmableLight)
     * @return the id of the device or null if it fails
     */
    public final Integer addDevice(@NotNull final Integer roomId, @NotNull DeviceType deviceType) {
        Device newDevice = DeviceType.makeDevice(deviceType, this, couplingService);
        if(newDevice == null) return  null;

        Room room = roomStorage.get(roomId);
        if (room == null) return null;

        Integer deviceId = deviceStorage.insert(newDevice);
        if (deviceId == null) return null;

        room.addDevice(deviceId);
        roomStorage.update(room);

        return deviceId;
    }


    /**
     * Deletes the device with the given Id from the room with the given Id.
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

    /**
     * Returns the  average temperature from all temperature sensors in a given room.
     *
     * @param roomId the id of the room
     * @return the average temperature
     */
    public double getAverageTemp(final Integer roomId, final Integer thermostatId) {
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
    }

}
