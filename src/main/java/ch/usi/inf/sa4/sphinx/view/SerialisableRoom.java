package ch.usi.inf.sa4.sphinx.view;

import ch.usi.inf.sa4.sphinx.model.Room;

public class SerialisableRoom {
    public int id;
    public String name;
    public String icon;
    public String background;
    public int[] devices;

    public SerialisableRoom(int id, String name, String icon, String background, int[] devices) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.background = background;
        this.devices = devices;
    }

    public SerialisableRoom(Room room) {
        this.id = room.id;
        this.name = room.name;
        this.icon = room.icon;
        this.background = room.background;
        for (int i = 0; i < this.devices.length; i++) {
            this.devices[i] = room.devices[i].id;
        }
    }
}
