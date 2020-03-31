package ch.usi.inf.sa4.sphinx.view;

import ch.usi.inf.sa4.sphinx.model.Room;

import java.util.Arrays;

public class SerialisableRoom {
    public Integer id;
    public String name;
    public String icon;
    public String background;
    public Integer[] devices;




    /** Constructor.**/
    public SerialisableRoom(){

    }

    /** Constructor.
     * @param name the name of the room
     * @param id the id of the room
     * @param background the background of the room
     * @param devices the list of device in that room (by id)
     * @param icon the icon of the room
     **/
    public SerialisableRoom(Integer id, String name, String icon, String background, Integer[] devices) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.background = background;
        this.devices = devices;
    }

    /** Constructor.
     * @param room an instance of Room
     **/
    public SerialisableRoom(Room room) {
        this.id = room.getId();
        this.name = room.getName();
        this.icon = room.getIcon();
        this.background = room.getBackground();
        this.devices = new Integer[room.getDevices().size()];
        for (int i = 0; i < this.devices.length; i++) {
            this.devices[i] = room.getDevices().get(i);
        }


    }
}
