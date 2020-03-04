package ch.usi.inf.sa4.sphinx.model;

import java.util.ArrayList;

public abstract class Device {
    public int id;
    public String icon;
    public String name;
    public ArrayList<Integer> ors;
    public ArrayList<Integer> xors;
    public boolean on;
    private static int nextId = 0;

    private static int makeId() { return nextId++; }

    public abstract String getLabel();

    public Device() {
        id = makeId();
        icon = "/images/generic_device";
        name = "Device";
        ors = new ArrayList<>();
        xors = new ArrayList<>();
        on = true;
    }

}