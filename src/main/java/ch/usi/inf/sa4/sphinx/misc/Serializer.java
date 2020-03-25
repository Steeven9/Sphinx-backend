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


public class Serializer {
    @Autowired
    private  UserService userService;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private RoomService roomService;

    private Serializer(){}

    /**
     * Serializes a Device for the desciption of the serialized fields consult SerialisableDevice
     * the field that can't be retrived from the device alone will be null
     * @param d the device to serialize
     * @return the serialized device
     */
    public  SerialisableDevice serializeDevice(Device d){
      return d.serialize();
    }

    /**
     * Serializes a device with additional info coming from the owner User:
     * the id of the room that owns the device
     * the name of the room that owns the device
     * the username of the user that owns the device
     * @param device the device to serialize
     * @param user the User that owns the device
     * @return
     */
    public  SerialisableDevice serializeDevice(Device device, User user){
        SerialisableDevice sd = serializeDevice(device);

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

    public SerialisableUser serializeUser(User user){
        return user.serialize();
    }

    public SerialisableRoom serializeRoom(Room room){
        return room.serialize();
    }

}
