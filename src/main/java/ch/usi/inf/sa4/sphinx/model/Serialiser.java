package ch.usi.inf.sa4.sphinx.model;


import ch.usi.inf.sa4.sphinx.service.DeviceService;
import ch.usi.inf.sa4.sphinx.view.SerialisableDevice;
import ch.usi.inf.sa4.sphinx.view.SerialisableRoom;
import ch.usi.inf.sa4.sphinx.view.SerialisableUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class that helps with the serialisation of models into views.
 */
@Component
public final class Serialiser {
    @Autowired
    private DeviceService deviceService;

    private Serialiser() {
    }

    /**
     * Serializes a device with additional info coming from the owner User:
     * the id of the room that owns the device
     * the name of the room that owns the device
     * the username of the user that owns the device
     *
     * @param device the device to serialize
     * @return the serialized device
     */
    public SerialisableDevice serialiseDevice(final Device device) {
        final SerialisableDevice sd = device.serialise();

        final Room owningRoom = device.getRoom();
        final User owningUser = owningRoom.getUser();
        sd.roomId = owningRoom.getId();
        sd.roomName = owningRoom.getName();
        sd.userName = owningUser.getUsername();
        sd.switched = deviceService.getSwitchedBy(device.getId()).stream().mapToInt(Integer::intValue).toArray();
        sd.switches = deviceService.getSwitches(device.getId()).stream().mapToInt(Integer::intValue).toArray();
        if(sd.switched.length == 0) sd.switched = null;
        if(sd.switches.length == 0) sd.switches = null;
        return sd;
    }

    /**
     * @param devices the Devices to serialise
     * @return a list of serialised devices with info about their owner
     * @see Serialiser#serialiseDevice(Device)
     */
    public List<SerialisableDevice> serialiseDevices(final Collection<? extends Device> devices) {
        return devices.stream().map(this::serialiseDevice).collect(Collectors.toList());
    }


    /**
     * Serialises a User. For the description of the serialised fields consult SerialisableUser.
     * fields whose value cannot be determined by looking at the User are set to null.
     *
     * @param user the user to serialize
     * @return the serialized user
     */
    public static SerialisableUser serialiseUser(final User user) {
        return user.serialise();
    }

    /**
     * Serialises a Room. For the description of the serialised fields consult SerialisableRoom.
     * Fields whose value cannot be determined by looking at the Room are set to null.
     *
     * @param room the room to serialize
     * @return the serialized room
     */
    public static SerialisableRoom serialiseRoom(final Room room) {
        return room.serialise();
    }


    /**
     * @param rooms the rooms to serialize
     * @return the serialized rooms
     * @see Serialiser#serialiseRoom(Room)
     */
    public static List<SerialisableRoom> serialiseRooms(final Collection<? extends Room> rooms) {
        return rooms.stream().map(Room::serialise).collect(Collectors.toList());
    }

}
