package ch.usi.inf.sa4.sphinx.view;

import ch.usi.inf.sa4.sphinx.model.*;
import ch.usi.inf.sa4.sphinx.misc.DeviceType;
import ch.usi.inf.sa4.sphinx.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

public class SerialisableDevice {
    @Autowired
    private static UserService userService;

    public Integer id;
    public String icon;
    public String name;
    public String label;
    public int[] switched;
    public int[] switches;
    public double slider;
    public Integer roomId;
    public String roomName;
    public Integer type;
    public String userName;
    public Boolean on;

    /** Constructor.**/
    public SerialisableDevice(){ }




    /** Constructor.
     * @param id the serialisable device's id
     * @param icon the serialisable device's icon
     * @param intensity the serialisable device's intensity
     * @param label the serialisable device's label
     * @param name the serialisable device's name
     * @param on tells whether the device is on
     * @param roomId the serialisable device's room id
     * @param switched list  (of ids) gitof switched  devices
     * @param switches list of switches (by id) that the device has
     * @param type the serialisable device's type
     **/
    public SerialisableDevice(Integer id, String icon, String name, String label, int[] switched, int[] switches, double intensity, Integer roomId, int type, boolean on) {
        this.id = id;
        this.icon = icon;
        this.name = name;
        this.label = label;
        this.switched = switched;
        this.switches = switches;
        this.slider = intensity;
        this.roomId = roomId;
        this.type = type;
        this.on = on;
    }

    /** Constructor.
     * @param device the device to be serialised
     * @param user the "owner" of the given device
     **/
    public SerialisableDevice(Device device, User user) {
        this.id = device.getId();
        this.icon = device.getIcon();
        this.name = device.getName();
        this.label = device.getLabel();
        this.on = device.isOn();


        var rooms = userService.getPopulatedRooms(user.getUsername());
        for(var room:rooms){
            if(room.getDevices().contains(device.getId())){
                this.roomId = room.getId();
            }
        }
        this.type = DeviceType.deviceTypetoInt(DeviceType.deviceToDeviceType(device));




        // TODO: fill switched
        //In the future discard this todo and use Serialiser class and serialise method instead
        if (device instanceof Switch || device instanceof DimmableSwitch) {
            // TODO: fill switches
        } else {
            this.switches = null;
        }
        if (device instanceof Dimmable) {
            this.slider = ((Dimmable)device).getState();
        }
    }


}
