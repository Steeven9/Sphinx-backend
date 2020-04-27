package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.misc.DeviceType;
import ch.usi.inf.sa4.sphinx.misc.NotImplementedException;
import ch.usi.inf.sa4.sphinx.view.SerialisableDevice;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.google.gson.annotations.Expose;
import ch.usi.inf.sa4.sphinx.service.CouplingService;
import ch.usi.inf.sa4.sphinx.service.RoomService;
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

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "room_id",
            nullable = false,
            referencedColumnName = "id"
    )
    private Room room;
    @Expose
    @Transient
    private final DeviceType deviceType;




    public Device() {
        icon = "./img/icons/devices/unknown-device.svg";
        name = "Device";
        on = true;
        this.couplings = new ArrayList<>();
        this.deviceType = getDeviceType();
    }




    /**
     * @return a copy of this Device
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


    public Room getRoom() {
        return room;
    }

    protected abstract DeviceType getDeviceType();



    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setName(String name) {
        this.name = name;
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

    /**
     * Adds an observer to this device that will be notified whenever its state changes.
     *
     * @param observer The observer to run when this device's state changes
     */
    public void addObserver(Coupling observer) {
        couplings.add(observer);
    }


    public void removeObserver(Coupling observer) {
        couplings.remove(observer);
    }


    //TODO fix unchecked
    protected void triggerEffects() {
        for (Coupling coupling : couplings) {
            coupling.run();
        }
    }

    public List<Coupling> getCouplings() {
        return couplings;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}