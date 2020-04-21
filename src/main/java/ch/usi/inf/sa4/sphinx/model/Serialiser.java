package ch.usi.inf.sa4.sphinx.model;


import ch.usi.inf.sa4.sphinx.service.DeviceService;
import ch.usi.inf.sa4.sphinx.service.RoomService;
import ch.usi.inf.sa4.sphinx.service.UserService;
import ch.usi.inf.sa4.sphinx.view.SerialisableDevice;
import ch.usi.inf.sa4.sphinx.view.SerialisableRoom;
import ch.usi.inf.sa4.sphinx.view.SerialisableUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class Serialiser {
    @Autowired
    private UserService userService;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private RoomService roomService;

    private Serialiser(){}

    /**
     * Serializes a Device for the description of the serialized fields consult SerialisableDevice
     * the field that can't be retrieved from the device alone will be null
     * @param d the device to serialize
     * @return the serialized device
     */
    public SerialisableDevice serialiseDevice(Device d){
        return d.serialise();
    }

    /**
     * Serializes a device with additional info coming from the owner User:
     * the id of the room that owns the device
     * the name of the room that owns the device
     * the username of the user that owns the device
     * @param device the device to serialize
     * @param user the User that owns the device
     * @return the serialized device
     */
    public  SerialisableDevice serialiseDevice(Device device, User user){
        SerialisableDevice sd = serialiseDevice(device);

        Room owningRoom = device.getRoom();
        User owningUser = owningRoom.getUser();
        sd.roomId = owningRoom.getId();
        sd.roomName = owningRoom.getName();
        sd.userName = owningUser.getUsername();
        return sd;
    }

    public List<SerialisableDevice> serialiseDevice(Collection<Device> devices, User user){
        return devices.stream().map(device -> serialiseDevice(device, user)).collect(Collectors.toList());
    }




    /**
     * Serialises a User. For the description of the serialised fields consult SerialisableUser.
     * fields whose value cannot be determined by looking at the User are set to null.
     * @param user the user to serialize
     * @return the serialized user
     */
    public SerialisableUser serialiseUser(User user){
        return user.serialise();
    }

    /**
     * Serialises a Room. For the description of the serialised fields consult SerialisableRoom.
     * Fields whose value cannot be determined by looking at the Room are set to null.
     * @param room the room to serialize
     * @return the serialized room
     */
    public SerialisableRoom serialiseRoom(Room room){
        return room.serialise();
    }


    public List<SerialisableRoom> serialiseRoom(Collection<Room> rooms){
        return rooms.stream().map(Room::serialise).collect(Collectors.toList());
    }

}
