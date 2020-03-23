package ch.usi.inf.sa4.sphinx.view;

import ch.usi.inf.sa4.sphinx.model.*;
import ch.usi.inf.sa4.sphinx.misc.DeviceType;
import ch.usi.inf.sa4.sphinx.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class SerialisableDevice {
    @Autowired
    private UserService userService;
    public int id;
    public String icon;
    public String name;
    public String label;
    public int[] switched;
    public int[] switches;
    public double intensity;
    public  int roomId;
    public int type;



    public SerialisableDevice(Integer id, String icon, String name, String label, int[] switched, int[] switches, double intensity, Integer roomId, int type) {
        this.id = id;
        this.icon = icon;
        this.name = name;
        this.label = label;
        this.switched = switched;
        this.switches = switches;
        this.intensity = intensity;
        this.roomId = roomId;
        this.type = type;
    }

    public SerialisableDevice(Device device, User user) {
        this.id = device.getId();
        this.icon = device.getIcon();
        this.name = device.getName();
        this.label = device.getLabel();

        var rooms = userService.getPopulatedRooms(user.getUsername());
        for(var room:rooms){
            if(room.getDevices().contains(device.getId())){
                this.roomId = room.getId();
            }
        }
        this.type = DeviceType.deviceTypeint(DeviceType.deviceToDeviceType(device));






        // TODO: fill switched
        if (device instanceof Switch || device instanceof DimmableSwitch) {
            // TODO: fill switches
        } else {
            this.switches = null;
        }
        if (device instanceof DimmableLight) {
            this.intensity = ((DimmableLight)device).getIntensity();
        }
        if (device instanceof DimmableSwitch) {
            this.intensity = ((DimmableSwitch)device).getState();
        }
    }
}
