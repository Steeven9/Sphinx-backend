package ch.usi.inf.sa4.sphinx.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public abstract class Device {
    public final int id;
    public String icon;
    public String name;
    public boolean on;
    protected List<Runnable> observers;

    private static int nextId = 0;

    private static int makeId() { return nextId++; }

    public abstract String getLabel();

    public Device() {
        id = makeId();
        icon = "/images/generic_device";
        name = "Device";
        on = true;
    }



    public void addObserver(Runnable observer) {
        observers.add(observer);
    }
    protected void triggerObservers() {
        for (Runnable observer : observers) {
            observer.run();
        }
    }


}