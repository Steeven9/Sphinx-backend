package ch.usi.inf.sa4.sphinx.misc;


import ch.usi.inf.sa4.sphinx.model.Device;
import ch.usi.inf.sa4.sphinx.model.Room;
import ch.usi.inf.sa4.sphinx.model.User;
import ch.usi.inf.sa4.sphinx.service.DeviceService;
import ch.usi.inf.sa4.sphinx.service.RoomService;
import ch.usi.inf.sa4.sphinx.service.UserService;
import ch.usi.inf.sa4.sphinx.view.SerialisableDevice;
import ch.usi.inf.sa4.sphinx.view.SerialisableRoom;
import ch.usi.inf.sa4.sphinx.view.SerialisableUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class Serialiser {
    @Autowired
    private static UserService userService;
    @Autowired
    private static DeviceService deviceService;
    @Autowired
    private static RoomService roomService;

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

        var rooms = userService.getPopulatedRooms(user.getUsername());
        for(var room:rooms){
            if(room.getDevices().contains(device.getId())){
                sd.roomId = room.getId();
                sd.roomName = room.getName();
                sd.userName = user.getUsername();
            }
        }
        return sd;
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

}
