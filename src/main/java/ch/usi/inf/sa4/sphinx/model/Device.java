package ch.usi.inf.sa4.sphinx.model;

import java.util.ArrayList;

public abstract class Device {
    protected String icon;
    protected String name;
    protected List<SwitchInterface> ors;
    protected List<SwitchInterface> xors;
    protected boolean on;

    public abstract String getLabel();

    public Device() {
        icon = "/images/generic_device";
        name = "Device";
        ors = new ArrayList<SwitchInterface>();
        xors = new ArrayList<SwitchInterface>();
        on = true;
    }

}