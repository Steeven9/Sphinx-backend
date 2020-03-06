package ch.usi.inf.sa4.sphinx.view;

import ch.usi.inf.sa4.sphinx.model.*;

import java.util.Map;

public class SerialisableDevice {
    public int id;
    public String icon;
    public String name;
    public String label;

    public Map<Integer, String> switched;
    public Map<Integer, String> switches;
    public int intensity;

    public SerialisableDevice(int id, String icon, String name, String label, int[] switched, int[] switches){
        this.id = id;
        this.icon = icon;
        this.name = name;
        this.label = label;
        //this.switched = switched;
        //this.switches = switches;
    }

    public SerialisableDevice(Device device, User user) {
        this.id = device.id;
        this.icon = device.icon;
        this.name = device.name;
        this.label = device.getLabel();
        // TODO: fill switched
        if (device instanceof Switch || device instanceof DimmableSwitch) {
            // TODO: fill switches
        } else {
            this.switches = null;
        }

        // TODO: set this.intensity based on the type of device
    }
}
