package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.misc.NotImplementedException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 *
 */
public abstract class Device {
    private  Integer id;
    private String icon;
    private String name;
    protected boolean on;
    protected List<Runnable> observers;



    public void setId(Integer id) {
        this.id = id;
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

    /**
     * Returns a user-facing description of the status of this device.
     * @return a human-friendly description of the current state of the device
     */
    public abstract String getLabel();

    public Device() {
        icon = "/images/generic_device";
        name = "Device";
        on = true;
    }

    //TODO
    /**
     * @return a copy of this Device
     */
     public Device makeCopy(){
        throw new NotImplementedException();
     }

    /**
     * Adds an observer to this device that will be notified whenever its state changes.
     * @param observer The observer to run when this device's state changes
     */
    public void addObserver(Runnable observer) {
        observers.add(observer);
    }
    protected void triggerObservers() {
        for (Runnable observer : observers) {
            observer.run();
        }
    }
}