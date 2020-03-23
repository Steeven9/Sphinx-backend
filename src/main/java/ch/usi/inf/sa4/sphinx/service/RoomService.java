package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.misc.NotImplementedException;
import ch.usi.inf.sa4.sphinx.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoomService {

@Autowired
RoomStorage roomStorage;
@Autowired
DeviceStorage deviceStorage;


    /**
<<<<<<< HEAD
     * @param roomId the roomId
     * @return Returns the Room with the given Id if present in the storage
     */
    public final Room get(final String roomId){
        return roomStorage.get(roomId);
    }

    /**
     * Updates the given user, the username is used to find the User and the given User to update its fields
     * @param room the room to update
     * @return true if successful update else false
     */
    public final boolean update(final Room room){
        return  roomStorage.update(room);
    }

    /**
     * Adds the type of device specified by deviceType to the Room with the given roomId
     * @param roomId id of the Room
     * @param deviceType the type of Device (ex DimmableLight)
     * @return true if success else false
     */
    public final String addDevice(final String roomId, String deviceType){
        Room room  = roomStorage.get(roomId);

        //Needs to know what type of device to generate based on deviceType
        throw new NotImplementedException();
    }

    /**
     * Deletes the device with the given Id from the room with the given Id
     * @param roomId   the id of the room
     * @param deviceId the id of the device
     * @return true if succes else false
     */
    public final boolean removeDevice(final String roomId, final String deviceId){
        Room room = roomStorage.get(roomId);
        if (room == null){
            return false;
        }
        if(!room.getDevices().remove(deviceId)){
            return false;
        }
        roomStorage.update(room);
        deviceStorage.delete(deviceId);
        return true;
    }

    /**
     * Given a room, return all the devices in this room.
     * @param roomId the id of the room
     * @return a list of all devices in this room
     */
    public List<Device> getDevices(final String roomId) {
        Room room = roomStorage.get(roomId);
        List<String> ls = room.getDevices();
        ArrayList<Device> list = new ArrayList<Device>();
        for (String id : ls) {
            list.add(deviceStorage.get(id));
        }
        return list;
    }

}
