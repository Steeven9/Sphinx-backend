package ch.usi.inf.sa4.sphinx.view;

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
}
