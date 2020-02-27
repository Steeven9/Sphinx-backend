package ch.usi.inf.sa4.sphinx.view;

import ch.usi.inf.sa4.sphinx.model.Device;

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

    public SerialisableDevice(Device device) {
        this.id = device.id;
        this.icon = device.icon;
        this.name = device.name;
        this.label = device.label;
        for (Integer key : device.switched.keySet()) {
            this.switched.put(key, device.switched.get(key));
        }
        for (Integer key : device.switches.keySet()) {
            this.switches.put(key, device.switches.get(key));
        }
    }
}
