package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.misc.DeviceType;
import ch.usi.inf.sa4.sphinx.view.SerialisableDevice;
import com.google.gson.annotations.Expose;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;



@Inheritance(strategy = InheritanceType.JOINED)
@Entity
public abstract class Device extends StorableE {

    @Expose
    private String icon;
    @Expose
    private String name;
    @Expose
    @Column(name = "active")
    protected boolean on; //DO NOT USE ON, IT'S RESERVED IN SQL!!!

    @OneToMany(orphanRemoval = false,
            cascade = CascadeType.PERSIST,
            fetch = FetchType.LAZY
    )
    protected final List<Coupling> couplings;

    @ManyToOne //TODO check why this had a merge cascade type
    @JoinColumn(name = "room_id",
            nullable = false,
            referencedColumnName = "id"
    )
    private Room room;
    @Expose
    @Transient
    private final DeviceType deviceType;


    /**
     * Creates a new device with default name and icon
     */
    public Device() {
        icon = "./img/icons/devices/unknown-device.svg";
        name = "Device";
        on = true;
        this.couplings = new ArrayList<>();
        this.deviceType = getDeviceType();
    }




    /**
     * @return a serialised copy of this Device
     * @see SerialisableDevice
     */
    protected SerialisableDevice serialise() {
        SerialisableDevice serialisableDevice = new SerialisableDevice();
        serialisableDevice.on = this.on;
        serialisableDevice.icon = this.icon;
        serialisableDevice.name = this.name;
        serialisableDevice.id = this.id;
        serialisableDevice.type = DeviceType.deviceTypetoInt(DeviceType.deviceToDeviceType(this));
        serialisableDevice.label = getLabel();
        return serialisableDevice;
    }


    /**
     * @return The Room that owns this device
     * @see Room
     */
    public Room getRoom() {
        return room;
    }

    /**
     * @return the DeviceType of this device
     * @see DeviceType
     */
    protected abstract DeviceType getDeviceType();


    /**
     * @param icon the icon to set
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * @return current icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * @return current name
     */
    public String getName() {
        return name;
    }

    /**
     * @return true if the Device is on else False
     */
    public boolean isOn() {
        return on;
    }

    /**
     * @param on true to turn on the Device false to turn it off
     */
    public void setOn(boolean on) {
        this.on = on;
    }

    /**
     * Returns a user-facing description of the status of this device.
     *
     * @return a human-friendly description of the current state of the device
     */
    public abstract String getLabel();

    /**
     * Adds an observer to this device that will be notified whenever its state changes.
     *
     * @param observer The observer to run when this device's state changes
     */
    public void addObserver(Coupling observer) {
        couplings.add(observer);
    }


    /**
     * Unlinks a Coupling from this Device.
     * @param observer the Coupling to remove
     */
    public void removeObserver(Coupling observer) {
        couplings.remove(observer);
    }


    /**
     * Triggers all Couplings linked to this Device
     * @see Coupling
     */
    //TODO fix unchecked
    protected void triggerEffects() {
        for (Coupling coupling : couplings) {
            coupling.run();
        }
    }

    /**
     * @return All Coupling linked to this Device
     */
    public List<Coupling> getCouplings() {
        return couplings;
    }

    /**
     * set the Room that owns this Device.
     * @param room the Room
     */
    public void setRoom(Room room) {
        this.room = room;
    }
}