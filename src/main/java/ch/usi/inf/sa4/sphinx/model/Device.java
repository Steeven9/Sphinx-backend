package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.misc.DeviceType;
import ch.usi.inf.sa4.sphinx.service.CouplingService;
import ch.usi.inf.sa4.sphinx.view.SerialisableDevice;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/** Class represents a device **/
public abstract class Device {

    @Autowired
    static CouplingService couplingService;

    private Integer id;
    private String icon;
    private String name;
    protected boolean on;
    protected List<Integer> couplings;

    /**
     * Constructor.
     */
    public Device() {
        icon = "/images/generic_device";
        name = "Device";
        on = true;

    }

    /**
     * Constructor.
     * @param d, the device
     */
    protected Device(Device d) {
        this.icon = d.getIcon();
        this.name = d.getName();
        this.on = d.isOn();
        this.couplings = new ArrayList<>(d.couplings);
        this.id = d.id;
    }


    /**
     * Creates copy of device.
     * @return a copy of this Device
     */
    public abstract Device makeCopy();

    /** Serialized a device.
     * @return a serialiasableDevice
     **/
    public SerialisableDevice serialise() {
        SerialisableDevice serialisableDevice = new SerialisableDevice();
        serialisableDevice.id = this.id;
        serialisableDevice.on = this.on;
        serialisableDevice.icon = this.icon;
        serialisableDevice.name = this.name;
        serialisableDevice.type = DeviceType.deviceTypetoInt(DeviceType.deviceToDeviceType(this));
        return serialisableDevice;
    }

    /** Setter for the id.
     * @return a true if the id has been set (from null to int), false otherwise
     **/
    public boolean setId(Integer id) {
        if (id == null) {
            this.id = id;
            return true;
        }
        return false;
    }
    /** Setter for the id.
     * @return a true if the id has been set (from null to int), false otherwise
     **/
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /** Setter for the name.
     * @param name the value to be set
     **/
    public void setName(String name) {
        this.name = name;
    }

    /** Getter for the id.
     * @return the id
     **/
    public Integer getId() {
        return id;
    }

    /** Getter for the icon.
     * @return the the icon
     **/
    public String getIcon() {
        return icon;
    }

    /** Getter for the name.
     * @return the name
     **/
    public String getName() {
        return name;
    }

    /** Tells if a device is on.
     * @return on if true
     **/
    public boolean isOn() {
        return on;
    }

    /** Set's device to on.
     * @param on , to set the device to
     **/
    public void setOn(boolean on) {
        this.on = on;
    }

    /**
     * Returns a user-facing description of the status of this device.
     * @return a human-friendly description of the current state of the device
     */
    public abstract String getLabel();

    /**
     * Adds an observer to this device that will be notified whenever its state changes.
     * @param observer The observer to run when this device's state changes
     */
    public void addObserver(Integer observer) {
        couplings.add(observer);
    }


    /** Triggers this effect
     **/
    //TODO fix unchecked
    protected void triggerEffects() {
        for(Integer coupling: couplings){
            Effect effect = couplingService.getEffect(coupling);
            Event event = couplingService.getEvent(coupling);
            effect.execute(event.get());
        }




    }
}