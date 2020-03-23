package ch.usi.inf.sa4.sphinx.view;

import ch.usi.inf.sa4.sphinx.model.*;

import java.util.Map;

public class SerialisableDevice {
    public String id;
    public String icon;
    public String name;
    public String label;
    public int[] switched;
    public int[] switches;
    public double intensity;

    public SerialisableDevice(String id, String icon, String name, String label, int[] switched, int[] switches){
        this.id = id;
        this.icon = icon;
        this.name = name;
        this.label = label;
        this.switched = switched;
        this.switches = switches;
    }

    public SerialisableDevice(Device device, User user) {
        this.id = device.getId();
        this.icon = device.getIcon();
        this.name = device.getName();
        this.label = device.getLabel();
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
