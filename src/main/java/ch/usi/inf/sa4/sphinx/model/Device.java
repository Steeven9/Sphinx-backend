package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.misc.DeviceType;
import ch.usi.inf.sa4.sphinx.service.CouplingService;
import ch.usi.inf.sa4.sphinx.view.SerialisableDevice;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;


public abstract class Device extends Storable<Integer, Device> {

    @Autowired
    static CouplingService couplingService;

    private String icon;
    private String name;
    protected boolean on;
    protected final List<Integer> couplings;

    public Device() {
        icon = "/images/generic_device";
        name = "Device";
        on = true;
        this.couplings = new ArrayList<>();

    }

    protected Device(Device d) {
        super.setKey(d.getKey());
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
        serialisableDevice.id = getKey();
        return serialisableDevice;
    }

    public boolean setId(Integer key) {
        return super.setKey(key);
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return getKey();
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
        couplings.add(observer);
    }


    //TODO fix unchecked
    protected void triggerEffects() {
        for (Integer coupling : couplings) {
            Effect effect = couplingService.getEffect(coupling);
            Event event = couplingService.getEvent(coupling);
            effect.execute(event.get());
        }
    }
<<<<<<< HEAD
=======
<<<<<<< HEAD


=======
>>>>>>> #61: added tests for Device, Dimmable and child classes. Improved room test.
>>>>>>> solved conflicts
}