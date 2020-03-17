package ch.usi.inf.sa4.sphinx.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public abstract class Device {
    private final int id;
    private String icon;
    private String name;
    private boolean on;
    protected List<Runnable> observers;

    private static int nextId = 0;

    private static int makeId() { return nextId++; }

    public int getId() {
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
        id = makeId();
        icon = "/images/generic_device";
        name = "Device";
        on = true;
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