package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.misc.DeviceType;
import ch.usi.inf.sa4.sphinx.misc.NotImplementedException;
import ch.usi.inf.sa4.sphinx.service.CouplingService;
import ch.usi.inf.sa4.sphinx.view.SerialisableDevice;
import com.google.gson.annotations.Expose;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Entity
public abstract class Device extends StorableE {

    @Autowired
    private CouplingService couplingService;

    @Expose
    private String icon;
    @Expose
    private String name;
    @Expose
    protected boolean on;
    @OneToMany(orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "device"
    )
    protected final List<Coupling> couplings;
    @JoinColumn(name = "room_id")
    private Room room;






    public Device() {
        icon = "/images/generic_device";
        name = "Device";
        on = true;
        this.couplings = new ArrayList<>();

    }

    protected Device(Device d) {
        //super.setKey(d.getKey());
        this.icon = d.getIcon();
        this.name = d.getName();
        this.on = d.isOn();
        this.couplings = new ArrayList<>(d.couplings);
    }


    /**
     * @return a copy of this Device
     */

    protected SerialisableDevice serialise() {
        SerialisableDevice serialisableDevice = new SerialisableDevice();
        serialisableDevice.on = this.on;
        serialisableDevice.icon = this.icon;
        serialisableDevice.name = this.name;
        serialisableDevice.type = DeviceType.deviceTypetoInt(DeviceType.deviceToDeviceType(this));
        //serialisableDevice.id = getKey();
        return serialisableDevice;
    }


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
    public void addObserver(Integer observer) {
        //couplings.add(observer);
    }


    //TODO fix unchecked
    protected void triggerEffects() {
//        for (Integer coupling : couplings) {
//            Effect effect = couplingService.getEffect(coupling);
//            Event event = couplingService.getEvent(coupling);
//            effect.execute(event.get());
//        }

        throw new NotImplementedException();
    }


    public void setRoom(Room room) {
        this.room = room;
    }
}