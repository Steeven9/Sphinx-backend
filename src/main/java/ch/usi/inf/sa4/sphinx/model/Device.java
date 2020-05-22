package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.misc.DeviceType;
import ch.usi.inf.sa4.sphinx.misc.ServiceProvider;
import ch.usi.inf.sa4.sphinx.service.DeviceService;
import ch.usi.inf.sa4.sphinx.view.SerialisableDevice;
import com.google.gson.annotations.Expose;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


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


    /**
     * Creates a new device with default name and icon
     */
    public Device() {
        icon = "./img/icons/devices/unknown-device.svg";
        name = "Device";
        on = true;
        this.couplings = new ArrayList<>();
    }


    /**
     * Serializes a list of devices.
     *
     * @param devices the Devices to serialise
     * @return a list of serialised devices with info about their owner
     * @see Device#serialise()
     */
    public static List<SerialisableDevice> serialise(final Collection<? extends Device> devices) {
        return devices.stream().map(Device::serialise).collect(Collectors.toList());
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


   public abstract DeviceType getDeviceType();


    /**
     * Sets properties of this device to conform with the given SerialisableDevice
     * @param sd the SerialisableDevice with the properties that are desired in this Device
     */
    public void setPropertiesFrom(final SerialisableDevice sd) {
        if (sd.getIcon() != null) icon = sd.getIcon();
        if (sd.getName() != null) name = sd.getName();
        if (sd.getOnState()!= null) setOn(sd.getOnState());
    }

    /**
     * @param icon the icon to set
     */
    public void setIcon(final String icon) {
        this.icon = icon;
    }

    /**
     * @param name the name to set
     */
    public void setName(final String name) {
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
    public void setOn(final boolean on) {
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
    public void addObserver(final Coupling observer) {
        couplings.add(observer);
    }


    /**
     * Unlinks a Coupling from this Device.
     * @param observer the Coupling to remove
     */
    public void removeObserver(final Coupling observer) {
        couplings.remove(observer);
    }


    /**
     * Triggers all Couplings linked to this Device
     * @see Coupling
     */
    //TODO fix unchecked
    protected void triggerEffects() {
        for (final Coupling coupling : couplings) {
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
     *
     * @param room the Room
     */
    public void setRoom(final Room room) {
        this.room = room;
    }

    /**
     * Serializes the device.
     *
     * @return a serialised copy of this Device
     * @see SerialisableDevice
     */
    public SerialisableDevice serialise() {
        final SerialisableDevice serialisableDevice = new SerialisableDevice();
        final DeviceService deviceService = ServiceProvider.getDeviceService();
        serialisableDevice.setOnState(this.on);
        serialisableDevice.setIcon(this.icon);
        serialisableDevice.setName(this.name);
        serialisableDevice.setId(this.id);
        serialisableDevice.setType(DeviceType.deviceTypetoInt(DeviceType.deviceToDeviceType(this)));
        serialisableDevice.setLabel(getLabel());
        final Room owningRoom = this.getRoom();
        final User owningUser = owningRoom.getUser();
        serialisableDevice.setRoomId(owningRoom.getId());
        serialisableDevice.setRoomName( owningRoom.getName());
        serialisableDevice.setUserName( owningUser.getUsername());
        serialisableDevice.setSwitchedIds(deviceService.getSwitchedBy(this.getId()).stream().mapToInt(Integer::intValue).toArray());
        serialisableDevice.setSwitchesIds(deviceService.getSwitches(this.getId()).stream().mapToInt(Integer::intValue).toArray());
        if (serialisableDevice.getSwitched().length == 0) serialisableDevice.setSwitchedIds(null);
        if (serialisableDevice.getSwitches().length == 0) serialisableDevice.setSwitchesIds(null);
        return serialisableDevice;
    }
}