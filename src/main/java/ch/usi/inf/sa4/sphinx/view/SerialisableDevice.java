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

    public SerialisableDevice(int id, String icon, String name, String label, Map<Integer, String> switched, Map<Integer, String> switches){
        this.id = id;
        this.icon = icon;
        this.name = name;
        this.label = label;
        this.switched = switched;
        this.switches = switches;
    }

    public SerialisableDevice(Device device, User user) {
        this.id = device.id;
        this.icon = device.icon;
        this.name = device.name;
        this.label = device.getLabel();
        for (SwitchInterface sw : device.ors) {
            this.switched.put(sw.id, "or");
        }
        for (SwitchInterface sw : device.xors) {
            this.switched.put(sw.id, "xor");
        }
        if (device instanceof Switch || device instanceof DimmableSwitch) {
            for (Room r : user.rooms) {
                for (Device d : r.devices) {
                    for (SwitchInterface si : d.ors) {
                        if (si.id == device.id) {
                            this.switches.put(d.id, "or");
                        }
                    }
                    for (SwitchInterface si : d.xors) {
                        if (si.id == device.id) {
                            this.switches.put(d.id, "xor");
                        }
                    }
                }
            }
        } else {
            this.switches = null;
        }
    }
}
