package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.misc.DeviceType;
import ch.usi.inf.sa4.sphinx.misc.NotImplementedException;
import ch.usi.inf.sa4.sphinx.service.CouplingService;
import ch.usi.inf.sa4.sphinx.view.SerialisableDevice;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
public abstract class Device {

    @Autowired
    static CouplingService couplingService;

    private Integer id;
    private String icon;
    private String name;
    protected boolean on;
    protected List<Integer> couplings;

    public Device() {
        icon = "/images/generic_device";
        name = "Device";
        on = true;
    }

    // TODO(lagraf): rewrite correctly or confirm that it is so already
    public Device(Device d) {
        this.icon = d.getIcon();
        this.name = d.getName();
        this.on = d.isOn();
        this.couplings = new ArrayList<>(d.getCouplings());
    }

    // Added only for the copy constructor
    // TODO: remove if its presence is undesired
    private List<Integer> getCouplings() {
        return couplings;
    }

    public SerialisableDevice serialise() {
        SerialisableDevice serialisableDevice = new SerialisableDevice();
        serialisableDevice.id = this.id;
        serialisableDevice.icon = this.icon;
        serialisableDevice.name = this.name;
        serialisableDevice.type = DeviceType.deviceTypetoInt(DeviceType.deviceToDeviceType(this));
        return serialisableDevice;
    }

    public void setId(Integer id) {
        if (id == null) {
            this.id = id;
        }
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    /**
     * Returns a user-facing description of the status of this device.
     *
     * @return a human-friendly description of the current state of the device
     */
    public abstract String getLabel();


    //TODO

    /**
     * @return a copy of this Device
     */
    public Device makeCopy() {
        throw new NotImplementedException();
    }

    /**
     * Adds an observer to this device that will be notified whenever its state changes.
     *
     * @param observer The observer to run when this device's state changes
     */
    public void addObserver(Integer observer) {
        couplings.add(observer);
    }


    //TODO fix unchecked
    protected void triggerEffects() {
        for(Integer coupling: couplings){
            Effect effect = couplingService.getEffect(coupling);
            Event event = couplingService.getEvent(coupling);
            effect.execute(event.get());
        }




    }
}